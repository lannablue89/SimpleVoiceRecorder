package com.example.lanna.simplevoicerecorder.fragment;

/**
 * Created by Lanna on 11/7/14.
 */

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.lanna.simplevoicerecorder.database.MyDatabase;
import com.example.lanna.simplevoicerecorder.helper.StoreAudioHelper;
import com.example.lanna.simplevoicerecorder.R;
import com.example.lanna.simplevoicerecorder.helper.Utilities;
import com.example.lanna.simplevoicerecorder.model.AudioModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordVoiceFragment extends Fragment {

    private ToggleButton mTbtnRecordVoice;

    private MediaRecorder mRecorder;
    private MyDatabase mDb;
    private AudioModel mAudioModel;

    private Uri mUri = Uri.parse(MyDatabase.URI_TO_NOTIFY_DATA_UPDATE);
    private long mTimeStart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record_voice, container, false);

        initView(rootView);
        initData();

        return rootView;
    }

    private void initView(View v) {
        mTbtnRecordVoice = (ToggleButton) v.findViewById(R.id.toggleButton_recordVoice);
        mTbtnRecordVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startRecordVoice();
                } else {
                    stopRecordVoice();
                }
            }
        });
    }

    private void initData() {
        mDb = new MyDatabase(getActivity());
    }

    private void startRecordVoice() {
        Date timeCreate = new Date();
        String filename = new SimpleDateFormat("ddMMyy-hhmmss").format(timeCreate);
        String filePath = StoreAudioHelper.createFileWithName(filename + StoreAudioHelper.AUDIO_RECORDER_FILE_EXT_MP3);
        if (filePath == null) {
            Utilities.makeToast(getActivity(), "no file for saving audio, do not record!!!");
            mTbtnRecordVoice.setChecked(false);
            return;
        }

        Utilities.makeToast(getActivity(), "start Record Voice...");
        mAudioModel = new AudioModel(filename, filePath, timeCreate.getTime());

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(StoreAudioHelper.getFileStoragePath(filePath));

        mRecorder.setOnErrorListener(errorListener);
        mRecorder.setOnInfoListener(infoListener);
        try {
            mRecorder.prepare();
            mTimeStart = System.currentTimeMillis();
            mRecorder.start(); // Recording is now started
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecordVoice() {
        Utilities.makeToast(getActivity(), "stop Record Voice!");
        if (null != mRecorder) {
            long duration = System.currentTimeMillis() - mTimeStart;

            mRecorder.stop();
            mRecorder.reset();   // You can reuse the object by going back to setAudioSource() step
            mRecorder.release(); // Now the object cannot be reused
            mRecorder = null;

            // save audio info
            mAudioModel.setDuration(duration);
            mDb.insertOrUpdateAudioAndNotify(getActivity(), mUri, mAudioModel, true);

            // test data after db action
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        StoreAudioHelper.writeDBToSD(getActivity(), mDb.DATABASE_NAME);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
        Utilities.makeToast(getActivity(), "Error: " + what + ", " + extra);
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
        Utilities.makeToast(getActivity(), "Warning: " + what + ", " + extra);
        }
    };

}