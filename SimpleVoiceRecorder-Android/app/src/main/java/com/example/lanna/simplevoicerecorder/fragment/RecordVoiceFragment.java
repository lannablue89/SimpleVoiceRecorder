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

import com.example.lanna.simplevoicerecorder.AudioFileHelper;
import com.example.lanna.simplevoicerecorder.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordVoiceFragment extends Fragment {

    private ToggleButton tbtnRecordVoice;

    private MediaRecorder recorder;

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
        String filename = AudioFileHelper.getFilename();
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