//to set context for data base
package com.example.adwatna1project;

import android.app.Application;

import com.firebase.client.Firebase;

public class FireApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
