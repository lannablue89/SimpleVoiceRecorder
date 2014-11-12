package com.example.lanna.simplevoicerecorder.model;

/**
 * Created by Lanna on 11/12/14.
 */
public class AudioModel {

    protected String name;

    protected long timeCreated;
    protected long timeLength;
    protected long timeCurrent;

    public AudioModel(String name, long timeCreated, long timeLength) {
        this.name = name;
        this.timeCreated = timeCreated;
        this.timeLength = timeLength;
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

    public long getTimeCurrent() {
        return timeCurrent;
    }

    public void setTimeCurrent(long timeCurrent) {
        this.timeCurrent = timeCurrent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
