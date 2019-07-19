package com.example.ricardoogliari.alarmesmartphone;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AlarmService {

    @GET("stopAlarm")
    Call<ReturnParser> stopAlarm();

}

