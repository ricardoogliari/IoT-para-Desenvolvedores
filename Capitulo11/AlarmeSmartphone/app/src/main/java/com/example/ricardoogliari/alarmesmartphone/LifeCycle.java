package com.example.ricardoogliari.alarmesmartphone;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LifeCycle extends Application {

    public static AlarmService service;

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-central1-alarmefirebase-31d5b.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(AlarmService.class);
    }
}
