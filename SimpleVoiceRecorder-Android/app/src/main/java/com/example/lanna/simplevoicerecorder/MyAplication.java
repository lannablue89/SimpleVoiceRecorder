package com.example.lanna.simplevoicerecorder;

import android.app.Application;
import android.content.Context;

/**
 * Created by Lanna on 11/11/14.
 */
public class MyAplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() { return context; }
}
