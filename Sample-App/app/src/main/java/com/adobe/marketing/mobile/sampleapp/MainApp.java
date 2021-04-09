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
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.Lifecycle;
import com.adobe.marketing.mobile.Analytics;
import com.adobe.marketing.mobile.Signal;
import com.adobe.marketing.mobile.UserProfile;
import com.adobe.marketing.mobile.InvalidInitException;
import com.adobe.marketing.mobile.LoggingMode;
import com.adobe.marketing.mobile.edge.consent.Consent;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class MainApp extends Application {

    private static final String LOG_TAG = "MainApp";
    private static final String LAUNCH_ENVIRONMENT_FILE_ID = "3805cb8645dd/b854d6a34ab5/launch-bdc2d5a05f2a-development";

    private static Context context;
    public static Context getAppContext(){
        return MainApp.context;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        MainApp.context = getApplicationContext();

        MobileCore.setApplication(this);
        MobileCore.setLogLevel(LoggingMode.VERBOSE);
        MobileCore.setSmallIconResourceID(R.mipmap.ic_launcher_round);
        MobileCore.setLargeIconResourceID(R.mipmap.ic_launcher_round);

        try{
            Analytics.registerExtension();
            UserProfile.registerExtension();
            Consent.registerExtension();
            com.adobe.marketing.mobile.Identity.registerExtension();
            com.adobe.marketing.mobile.edge.identity.Identity.registerExtension();
            Lifecycle.registerExtension();
            Signal.registerExtension();
            Edge.registerExtension();
            Assurance.registerExtension();

            MobileCore.configureWithAppID(LAUNCH_ENVIRONMENT_FILE_ID);
            MobileCore.start(new AdobeCallback () {

                    @Override
                    public void call(Object o) {
                        Map<String, Object> config = new HashMap<>();
                        config.put("edge.configId", "d3d079e7-130e-4ec1-88d7-c328eb9815c4");

                        MobileCore.updateConfiguration(config);
                        Log.d(LOG_TAG, "AEP Mobile SDK is initialized");

                    }
                });
                 } catch (InvalidInitException e) {
                    e.printStackTrace();
            }
    }
}
