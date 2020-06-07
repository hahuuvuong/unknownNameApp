package com.example.sach;

import android.app.Application;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "font/font_1.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        //TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "font/roboto_regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
    }
}