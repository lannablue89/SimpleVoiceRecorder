package com.example.lanna.simplevoicerecorder.model;

import android.content.Context;
import android.database.Cursor;

import com.example.lanna.simplevoicerecorder.database.MyDatabase;
import com.example.lanna.simplevoicerecorder.helper.StoreAudioHelper;

/**
 * Created by Lanna on 11/12/14.
 */
public class AudioModel {

    protected long audioID;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    protected String name;
    protected String filename;

    protected long timeCreated;
    protected long timeLength;
    protected long currentProgress;

    public AudioModel(String name, String filename, long timeCreated) {
        this.name = name;
        this.filename = filename;
        this.timeCreated = timeCreated;
        this.currentProgress = 0;
    }

    public AudioModel(Context context, Cursor c) {
        this.audioID = c.getLong(c.getColumnIndex(MyDatabase.FLD_ID_ALIAS));
        this.name = c.getString(c.getColumnIndex(MyDatabase.FLD_AUDIO_NAME));
        this.filename = c.getString(c.getColumnIndex(MyDatabase.FLD_AUDIO_FILENAME));
        this.timeCreated = c.getLong(c.getColumnIndex(MyDatabase.FLD_AUDIO_CREATED));
        this.currentProgress = c.getLong(c.getColumnIndex(MyDatabase.FLD_AUDIO_CURRENT_PROGRESS));
        updateTimeLength(context);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("audioID:").append(audioID)
                .append(", name:").append(name)
//                .append(", filename:").append(filename)
//                .append(", timeCreated:").append(timeCreated)
//                .append(", currentProgress:").append(currentProgress)
                .toString();
    }

    public long getAudioID() {
        return audioID;
    }

    public void setAudioID(long audioID) {
        this.audioID = audioID;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(long timeLength) {
        this.timeLength = timeLength;
    }

    public void updateTimeLength(Context context) {
        if (context != null) {
            this.timeLength = StoreAudioHelper.getDuration(context, this.name); // TODO
        } else {
            this.timeLength = 0;
        }
    }

    public long getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(long currentProgress) {
        this.currentProgress = currentProgress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
