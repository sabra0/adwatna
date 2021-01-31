package com.example.adwatna1project;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessegingService extends FirebaseMessagingService {

    private static final String TAG = "MyMessigingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG,"from: "+remoteMessage.getFrom());
        if(remoteMessage.getData().size()>0){
            Log.d(TAG,"message data payload: "+remoteMessage.getData());
            sendNotification(remoteMessage);

        }

    }
    private void sendNotification(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        String title = data.get("title");
        String content = data.get("content");

        Intent intent = new Intent(this, DisplayChatWithUsersActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, /*(int) when*/1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "MyId";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            @SuppressLint("WrongConstant") NotificationChannel notificationChannel
                    = new NotificationChannel
                    (NOTIFICATION_CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_MAX);

            notificationChannel.setDescription("My channel for test FCM");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setTicker("Ticker")
                .setContentTitle(title)
                .setContentText(content)
                .setContentInfo("info")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(1, notificationBuilder.build());

    }
}
