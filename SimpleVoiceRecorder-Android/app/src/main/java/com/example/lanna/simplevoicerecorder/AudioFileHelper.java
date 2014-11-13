package com.example.lanna.simplevoicerecorder;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lanna on 11/13/14.
 */
public class AudioFileHelper {

    private static final String AUDIO_RECORDER_FILE_EXT_MP3 = ".mp3";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";

    public static String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File folder = new File(filepath, AUDIO_RECORDER_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String currentTimeString = new SimpleDateFormat("ddMMyy-hhmmss").format(new Date());
        String filename = String.format("%s/%s%s", folder.getAbsolutePath(), currentTimeString, AUDIO_RECORDER_FILE_EXT_MP3);

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

    public static String getFilePath(String audioName) {
        return Environment.getExternalStorageDirectory().getPath() + "/" + audioName;
    }

}
