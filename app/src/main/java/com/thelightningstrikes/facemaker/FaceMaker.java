package com.thelightningstrikes.facemaker;

import android.app.Application;
import android.content.Context;

public class FaceMaker extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        FaceMaker.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return FaceMaker.context;
    }
}