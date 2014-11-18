package com.example.lanna.simplevoicerecorder.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Lanna on 11/18/14.
 */
public class Utilities {

    public static void makeToast(Context context, String mes) {
        Toast.makeText(context, mes, Toast.LENGTH_SHORT).show();
        Log.i("lanna", mes);
    }

}
