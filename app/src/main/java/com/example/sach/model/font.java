package com.example.sach.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;

public class font {
    private static final String TAG = "font";
    public static void setFontSize(Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences("Settings",activity.MODE_PRIVATE);
        Float sizeFont= prefs.getFloat("my_fontsize", 1);
        Log.e(TAG, "setFontSize: "+sizeFont );

        Configuration config = new Configuration();

        config.fontScale =  sizeFont;
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        metrics.scaledDensity = config.fontScale * metrics.density;
        activity.getBaseContext().getResources().updateConfiguration(config, metrics);

        SharedPreferences.Editor editor = activity.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putFloat("my_fontsize",sizeFont);
        editor.apply();
    }
}
