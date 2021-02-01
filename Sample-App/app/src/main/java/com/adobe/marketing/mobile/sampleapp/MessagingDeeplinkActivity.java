package com.adobe.marketing.mobile.sampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.adobe.marketing.mobile.Messaging;

public class MessagingDeeplinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_deeplink);

        // Tracking push notification
        Messaging.handleNotificationResponse(getIntent(), true, null);
    }
}