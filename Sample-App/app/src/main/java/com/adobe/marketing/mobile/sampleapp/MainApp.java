/*
 Copyright 2020 Adobe
 All Rights Reserved.

 NOTICE: Adobe permits you to use, modify, and distribute this file in
 accordance with the terms of the Adobe license agreement accompanying
 it.
 */
package com.adobe.marketing.mobile.sampleapp;

import com.adobe.marketing.mobile.AdobeCallback;

import com.adobe.marketing.mobile.Assurance;
import com.adobe.marketing.mobile.Edge;
import com.adobe.marketing.mobile.Messaging;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.Lifecycle;
import com.adobe.marketing.mobile.Analytics;
import com.adobe.marketing.mobile.Signal;
import com.adobe.marketing.mobile.UserProfile;
import com.adobe.marketing.mobile.InvalidInitException;
import com.adobe.marketing.mobile.LoggingMode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class MainApp extends Application {

    private static final String LOG_TAG = "MainApp";
//    private static final String LAUNCH_ENVIRONMENT_FILE_ID = "";
//    static final String PLATFORM_DCS_URL = "";
//    // Profile dataset id used by messaging for syncing the push toke with profile
//    static final String PLATFORM_PROFILE_DATASET_ID = "";
//    // Org id needed by the messaging tab to send the custom event
//    static final String ORG_ID = "";
//    // Experience event dataset id used by messaging for sending tracking data
//    private static final String PLATFORM_EXPERIENCE_EVENT_DATASET_ID = "";
//    // Dataset id used by the messaging tab to send a custom action event.
//    static final String CUSTOM_ACTION_DATASET = "";

    private static final String LAUNCH_ENVIRONMENT_FILE_ID="3149c49c3910/6a68c2e19c81/launch-4b2394565377-development";
    static final String PLATFORM_DCS_URL="https://dcs.adobedc.net/collection/e28b94f7b68e32480ab10e4880bedb7a51c17b54e2275978cff915ae61f28070";
    static final String PLATFORM_PROFILE_DATASET_ID="601460f70085b8194b6aac02";
    static final String ORG_ID="906E3A095DC834230A495FD6@AdobeOrg";
    static final String PLATFORM_EXPERIENCE_EVENT_DATASET_ID="6014611ed9a0041949769e17";
    static final String CUSTOM_ACTION_DATASET="60146f1f6fb6ae19481833cb";
    static final String CUSTOM_PROFILE_DATASET="601a5860812a35194ac49baa";

    @Override
    public void onCreate() {
        super.onCreate();
        MobileCore.setApplication(this);
        MobileCore.setLogLevel(LoggingMode.VERBOSE);
        MobileCore.setSmallIconResourceID(R.mipmap.ic_launcher_round);
        MobileCore.setLargeIconResourceID(R.mipmap.ic_launcher_round);

        try {
            Analytics.registerExtension();
            UserProfile.registerExtension();
            Identity.registerExtension();
            Lifecycle.registerExtension();
            Signal.registerExtension();
            Edge.registerExtension();
            Assurance.registerExtension();
            Messaging.registerExtension();

            MobileCore.start(new AdobeCallback() {
                @Override
                public void call(Object o) {

                    MobileCore.configureWithAppID(LAUNCH_ENVIRONMENT_FILE_ID);

                    MobileCore.lifecycleStart(null);

                    Map<String, Object> map = new HashMap<>();
                    map.put("messaging.dccs", PLATFORM_DCS_URL);
                    map.put("messaging.profileDataset", PLATFORM_PROFILE_DATASET_ID);
                    map.put("messaging.eventDataset", PLATFORM_EXPERIENCE_EVENT_DATASET_ID);
                    MobileCore.updateConfiguration(map);
                    Log.d(LOG_TAG, "AEP Mobile SDK is initialized");

                    // To connect with Assurance session on start, put the URL for your session here
                    // Currently hardcoded is 'testAEPSampleArchanaSession1'
                    Assurance.startSession("aepsdksampleapp://?adb_validation_sessionid=2898a9e3-a9ae-47cb-a2e0-7bd267fa70e5");
                }
            });
        } catch (InvalidInitException e) {
            e.printStackTrace();
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(LOG_TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        MobileCore.setPushIdentifier(token);
                    }
                });
    }
}
