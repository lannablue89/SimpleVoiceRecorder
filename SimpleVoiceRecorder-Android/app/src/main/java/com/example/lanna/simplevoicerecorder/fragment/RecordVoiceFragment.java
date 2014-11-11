package com.example.lanna.simplevoicerecorder.fragment;

/**
 * Created by Lanna on 11/7/14.
 */

import android.support.v4.app.Fragment;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.lanna.simplevoicerecorder.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordVoiceFragment extends Fragment {

    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp3";
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };

    private ToggleButton tbtnRecordVoice;

    private MediaRecorder recorder;
    private int currentFormat = 0;

    public RecordVoiceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record_voice, container, false);

        tbtnRecordVoice = (ToggleButton) rootView.findViewById(R.id.toggleButton_recordVoice);
        tbtnRecordVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startRecordVoice();
                } else {
                    stopRecordVoice();
                }
            }
        });

        return rootView;
    }

    private void startRecordVoice() {
        String filename = getFilename();
        if (null == filename) {
            makeToast("no file for saving audio, do not record!!!");
            tbtnRecordVoice.setChecked(false);
            return;
        }

        makeToast("start Record Voice...");
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(filename);

        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);
        try {
            recorder.prepare();
            recorder.start(); // Recording is now started
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecordVoice() {
        makeToast("stop Record Voice!");
        if (null != recorder) {
            recorder.stop();
            recorder.reset();   // You can reuse the object by going back to setAudioSource() step
            recorder.release(); // Now the object cannot be reused
            recorder = null;
        }
    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File folder = new File(filepath, AUDIO_RECORDER_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String currentTimeString = new SimpleDateFormat("ddMMyy-hhmmss").format(new Date());
        String filename = String.format("%s/%s%s", folder.getAbsolutePath(), currentTimeString, file_exts[currentFormat]);

        File file = new File(filename);
        if(!file.exists()) {
            try {
                file.createNewFile();
                Log.i("lanna", "created file for saving at " + file);
                return filename;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.i("lanna", "fail to create file for saving at " + file);
        return null;
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