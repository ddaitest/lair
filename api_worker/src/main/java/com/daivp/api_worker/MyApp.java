package com.daivp.api_worker;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by qdhl on 2017/8/4.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
