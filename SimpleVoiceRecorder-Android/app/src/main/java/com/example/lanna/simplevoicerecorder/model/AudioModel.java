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
    protected String name;
    protected String filePath;

    protected long timeCreated;
    protected long duration;

    public AudioModel(String name, String filename, long timeCreated) {
        this.name = name;
        this.filePath = filename;
        this.timeCreated = timeCreated;
    }

    public AudioModel(Cursor c) {
        this.audioID = c.getLong(c.getColumnIndex(MyDatabase.FLD_ID_ALIAS));
        this.name = c.getString(c.getColumnIndex(MyDatabase.FLD_AUDIO_NAME));
        this.filePath = c.getString(c.getColumnIndex(MyDatabase.FLD_AUDIO_FILENAME));
        this.timeCreated = c.getLong(c.getColumnIndex(MyDatabase.FLD_AUDIO_CREATED));
        this.duration = c.getLong(c.getColumnIndex(MyDatabase.FLD_AUDIO_DURATION));
    }

    @Override
    public String toString() {
        return new StringBuilder()
//                .append("id:").append(audioID)
                .append(", name:").append(name)
//                .append(", filename:").append(filePath)
//                .append(", timeCreated:").append(timeCreated)
                .append(", duration:").append(duration)
                .toString();
    }

    public long getAudioID() {
        return audioID;
    }

    public void setAudioID(long audioID) {
        this.audioID = audioID;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
