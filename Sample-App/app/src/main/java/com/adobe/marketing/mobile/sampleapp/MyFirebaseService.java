package com.adobe.marketing.mobile.sampleapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.adobe.marketing.mobile.Messaging;
import com.adobe.marketing.mobile.MobileCore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseService extends FirebaseMessagingService {
    private final String CHANNEL_ID = "messaging_notification_channel";
    private final String CHANNEL_NAME = "Messaging Notifications Channel";

    public MyFirebaseService() {}

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        MobileCore.setPushIdentifier(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        String title = null;
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
        }


        String body = null;
        if (remoteMessage.getNotification() != null) {
            body = remoteMessage.getNotification().getBody();
        }

        Map<String, String> data = remoteMessage.getData();

        Intent intent;

        if (data.containsKey("deeplink")) {
            intent = new Intent(this, MessagingDeeplinkActivity.class);
        } else {
            intent = new Intent(this, MenuActivity.class);
        }

        Messaging.addPushTrackingDetails(intent, remoteMessage.getMessageId(), data);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;
        notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        notificationManager.notify(new Random().nextInt(100), notificationBuilder.build());
    }
}
