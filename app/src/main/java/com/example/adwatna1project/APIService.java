package com.example.adwatna1project;

import com.example.adwatna1project.Notifications.MyResponse;
import com.example.adwatna1project.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAVdFniP0:APA91bFJ22DlrOVyI5k1nyqih4lGVgwIKHuFfX5nTbz4RTon7DssTV3fF5jCU6daS1sK7RzRUCWpjKKAdXqqKDI07CZ3tthUElOB0tstLXO4Ck0OFRJbF7NpCwlhSp8QXFzaPCl1DDxI"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
