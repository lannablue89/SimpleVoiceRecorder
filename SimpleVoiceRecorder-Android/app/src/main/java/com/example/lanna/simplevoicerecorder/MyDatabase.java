package com.example.lanna.simplevoicerecorder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Lanna on 11/12/14.
 */
public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "simple_recorder.sqlite";
    private static final int DATABASE_VERSION = 1;

    public static String TBL_AUDIO = "Audio";

    public static String FLD_COMMON_ID              = "0 _id";

    public static String FLD_AUDIO_NAME             = "name";
    public static String FLD_AUDIO_CREATED          = "timeCreated";
    public static String FLD_AUDIO_CURRENT_PROGRESS = "currentProgress";

    public static byte FLD_AUDIO_INDEX_ID               = 0;
    public static byte FLD_AUDIO_INDEX_NAME             = 1;
    public static byte FLD_AUDIO_INDEX_CREATED          = 2;
    public static byte FLD_AUDIO_INDEX_CURRENT_PROGRESS = 3;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getAudios() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String sqlTables = TBL_AUDIO;
        String [] sqlSelect = { FLD_COMMON_ID, FLD_AUDIO_NAME, FLD_AUDIO_CREATED, FLD_AUDIO_CURRENT_PROGRESS };

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        c.moveToFirst();
        return c;

    }
}
