package com.example.serene;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;

public class SereneApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Log.d("SERENE_APP", " Firebase initialized in SereneApp");
    }
}
