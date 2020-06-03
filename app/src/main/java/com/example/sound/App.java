package com.example.sound;

import android.app.Application;

public class App extends Application {

    private static App mInstance;

    public static App getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("Application is not created.");
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
