package com.example.lanna.simplevoicerecorder.model;

/**
 * Created by Lanna on 11/12/14.
 */
public class AudioModel {

    protected String name;

    protected long timeCreated;
    protected long timeLength;
    protected long currentProgress;

    public AudioModel(String name, long timeCreated) {
        this.name = name;
        this.timeCreated = timeCreated;
        this.timeLength = 0;
        this.currentProgress = 0;
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
