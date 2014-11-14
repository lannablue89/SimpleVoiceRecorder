package com.example.lanna.simplevoicerecorder.fragment;

/**
 * Created by Lanna on 11/7/14.
 */

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record_voice, container, false);

        mTbtnRecordVoice = (ToggleButton) rootView.findViewById(R.id.toggleButton_recordVoice);
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

        mDb = new MyDatabase(getActivity());

        return rootView;
    }

    private void startRecordVoice() {
        Date timeCreate = new Date();
        String filename = new SimpleDateFormat("ddMMyy-hhmmss").format(timeCreate);
        String filePath = StoreAudioHelper.createFileWithName(filename);
        if (filePath == null) {
            makeToast("no file for saving audio, do not record!!!");
            mTbtnRecordVoice.setChecked(false);
            return;
        }

        makeToast("start Record Voice...");
        mAudioModel = new AudioModel(filename, filename, timeCreate.getTime());

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(filePath);

        mRecorder.setOnErrorListener(errorListener);
        mRecorder.setOnInfoListener(infoListener);
        try {
            mRecorder.prepare();
            mRecorder.start(); // Recording is now started
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecordVoice() {
        makeToast("stop Record Voice!");
        if (null != mRecorder) {
            mRecorder.stop();
            mRecorder.reset();   // You can reuse the object by going back to setAudioSource() step
            mRecorder.release(); // Now the object cannot be reused
            mRecorder = null;

            // save audio info
            mDb.insertOrUpdateAudio(mAudioModel);

            // test data after db action
//            try {
//                StoreAudioHelper.writeDBToSD(getActivity(), mDb.DATABASE_NAME);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void makeToast(String mes) {
        Toast.makeText(getActivity(), mes, Toast.LENGTH_SHORT).show();
    }

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            makeToast("Error: " + what + ", " + extra);
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            makeToast("Warning: " + what + ", " + extra);
        }
    };

}