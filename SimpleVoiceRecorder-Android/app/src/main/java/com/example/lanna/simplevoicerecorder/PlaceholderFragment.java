package com.example.lanna.simplevoicerecorder;

/**
 * Created by Lanna on 11/7/14.
 */

import android.app.Fragment;
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

import java.io.File;
import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };

    private ToggleButton tbtnRecordVoice;

    private MediaRecorder recorder;
    private int currentFormat = 0;

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

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
        makeToast("start Record Voice...");
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        String filename = getFilename();
        if (null == filename) {
            makeToast("no file for saving audio, stop record!!!");
            return;
        }
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

    private void makeToast(String mes) {
         Toast.makeText(getActivity(), mes, Toast.LENGTH_SHORT).show();
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
            Log.i("lanna", "folder.exists()=" + folder.exists());
        }
        String filename = System.currentTimeMillis() + file_exts[currentFormat];

        File file = new File(folder.getAbsolutePath(), filename);

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

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Toast.makeText(getActivity(), "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Toast.makeText(getActivity(), "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
        }
    };

}