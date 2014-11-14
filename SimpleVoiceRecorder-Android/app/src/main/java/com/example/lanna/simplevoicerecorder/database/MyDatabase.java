package com.example.lanna.simplevoicerecorder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.example.lanna.simplevoicerecorder.model.AudioModel;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Lanna on 11/12/14.
 */
public class MyDatabase extends SQLiteAssetHelper {

    public static final String DATABASE_NAME = "simple_recorder.sqlite";
    private static final int DATABASE_VERSION = 1;

    public static String TBL_AUDIO = "Audio";

    public static String FLD_ID_ALIAS               = "_id"; // used in CursorAdapter
    public static String FLD_ROW_ID                 = "rowid " + FLD_ID_ALIAS;

    public static String FLD_AUDIO_ID               = "audio_id";
    public static String FLD_AUDIO_NAME             = "name";
    public static String FLD_AUDIO_FILENAME         = "file_name";
    public static String FLD_AUDIO_CREATED          = "created";
    public static String FLD_AUDIO_CURRENT_PROGRESS = "current_progress";

    public static byte FLD_AUDIO_ID_INDEX               = 0;
    public static byte FLD_AUDIO_NAME_INDEX             = 1;
    public static byte FLD_AUDIO_CREATED_INDEX          = 2;
    public static byte FLD_AUDIO_CURRENT_PROGRESS_INDEX = 3;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getAudios() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = { FLD_ROW_ID, FLD_AUDIO_NAME, FLD_AUDIO_FILENAME, FLD_AUDIO_CREATED, FLD_AUDIO_CURRENT_PROGRESS };

        qb.setTables(TBL_AUDIO);
        Cursor c = qb.query(db, sqlSelect, FLD_AUDIO_FILENAME + " IS NOT NULL ", null, null, null, null);

        return c;
    }

    public long insertOrUpdateAudio(AudioModel audioModel) {
        long result = -1;
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FLD_AUDIO_NAME, audioModel.getName());
        values.put(FLD_AUDIO_FILENAME, audioModel.getFilename());
        values.put(FLD_AUDIO_CREATED, audioModel.getTimeCreated());
        values.put(FLD_AUDIO_CURRENT_PROGRESS, audioModel.getCurrentProgress());

        if (audioModel.getAudioID() > 0) {
            result = db.update(TBL_AUDIO, values, FLD_AUDIO_ID + "=" + audioModel.getAudioID(), null);
            Log.i("lanna", "updated at " + result + ", audio " + audioModel);
        } else {
            result = db.insert(TBL_AUDIO, null, values);
            audioModel.setAudioID(result);
            Log.i("lanna", "insert at " + result + ", audio " + audioModel);
        }
        db.close();
        return result;
    }

    public int deleteAudio(AudioModel audioModel) {
        int result = -1;
        SQLiteDatabase db = getWritableDatabase();

        if (audioModel.getAudioID() > 0) {
            result = db.delete(TBL_AUDIO, FLD_AUDIO_ID + "=" + audioModel.getAudioID(), null);
        }

        return result;
    }

}
