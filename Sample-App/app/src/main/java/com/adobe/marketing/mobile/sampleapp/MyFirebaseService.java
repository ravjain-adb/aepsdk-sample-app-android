package com.adobe.marketing.mobile.sampleapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.adobe.marketing.mobile.Messaging;
import com.adobe.marketing.mobile.MobileCore;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

        Map<String, String> data = remoteMessage.getData();

        String title = null;
        if (data.get("adb_title") != null) {
            title = data.get("adb_title");
            Log.d("MyFirebaseService", title);
        }


        String body = null;
        if (data.get("adb_body") != null) {
            body = data.get("adb_body");
            Log.d("MyFirebaseService", body);
        }

        Intent intent;

        if (data.containsKey("adb_uri") && data.containsKey("adb_a_type") && "DEEPLINK".equals(data.get("adb_a_type"))) {
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

        if (data.containsKey("adb_image")) {
            String url = data.get("adb_image");
            if (url!= null && !url.isEmpty()) {
                Future<Bitmap> bitmapTarget = Glide.with(this).asBitmap().load(url).submit();
                try {
                    Bitmap image = bitmapTarget.get();
                    notificationBuilder.setLargeIcon(image).setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).bigLargeIcon(null));
                } catch (ExecutionException | InterruptedException e) {
                    Log.d("MyFirebaseService", e.getLocalizedMessage());
                }
            }
        }

        if (data.containsKey("adb_act")) {
            String buttons = data.get("adb_act");
            Gson gson = new Gson();
            JsonArray array = gson.fromJson(buttons, JsonArray.class);
            for (int i = 0; i< array.size(); i++) {
                JsonObject obj = array.get(i).getAsJsonObject();
                String buttonName = obj.get("label").getAsString();
                notificationBuilder.addAction(new NotificationCompat.Action(R.drawable.ic_assurance_active, buttonName, null));
            }
        }

        notificationManager.notify(new Random().nextInt(100), notificationBuilder.build());
    }
}
