package com.mj.demkito;

import android.app.Application;

import com.mj.utils.Remember;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Remember.init(getApplicationContext(), "com.mj.demkito");
    }
}
