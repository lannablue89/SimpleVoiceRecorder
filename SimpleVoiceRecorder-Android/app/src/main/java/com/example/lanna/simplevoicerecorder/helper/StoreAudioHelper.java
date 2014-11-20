package com.example.lanna.simplevoicerecorder.helper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Lanna on 11/13/14.
 */
public class StoreAudioHelper {

    public static final String AUDIO_RECORDER_FILE_EXT_MP3      = ".mp3";
    private static final String AUDIO_RECORDER_FOLDER           = "AudioRecorder";
    private static final String AUDIO_RECORDER_STORAGE_FOLDER   = Environment.getExternalStorageDirectory() + "/" + AUDIO_RECORDER_FOLDER;

    public static String createFileWithName(String filename) {
        // check and create if not exist folder for saving files
        File folder = new File(AUDIO_RECORDER_STORAGE_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filePath = String.format("%s/%s", AUDIO_RECORDER_FOLDER, filename);
        String fullFileStoragePath = getFileStoragePath(filePath);
        File file = new File(fullFileStoragePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Log.i("lanna", "created file for saving at " + file);
                return filePath;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.i("lanna", "fail to create file for saving at " + file);
        return null;
    }

    public static String getFileStoragePath(String filePath) {
        return String.format("%s/%s",
                Environment.getExternalStorageDirectory(), filePath);
    }

    public static void writeDBToSD(Context context, String DB_NAME) throws IOException {
        String DB_PATH;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DB_PATH = context.getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator;
//        }
//        else {
//            DB_PATH = context.getFilesDir().getPath() + context.getPackageName() + "/databases/";
//        }

        File sd = new File(AUDIO_RECORDER_STORAGE_FOLDER);
        if (sd.canWrite()) {
            String currentDBPath = DB_NAME;
            File currentDB = new File(DB_PATH, currentDBPath);

            String backupDBPath = "backup_of_" + DB_NAME;
            File backupDB = new File(sd, backupDBPath);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
            Log.i("lanna", "writeDBToSD done");
        }
    }
}
