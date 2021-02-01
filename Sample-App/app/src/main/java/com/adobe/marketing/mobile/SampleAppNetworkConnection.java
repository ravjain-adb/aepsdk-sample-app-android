package com.adobe.marketing.mobile;

import java.util.Collections;
import java.util.Map;

import static com.adobe.marketing.mobile.MessagingConstant.LOG_TAG;

public class SampleAppNetworkConnection {

    public void connectPostUrl(String url, byte[] payload) {
        PlatformServices platformServices = new AndroidPlatformServices();
        NetworkService networkService = platformServices.getNetworkService();
        final NetworkService.HttpConnection connection = networkService.connectUrl(url, NetworkService.HttpCommand.POST, payload, Collections.singletonMap("Content-Type", "application/json"), 10, 10);
        if (connection == null) {
            Log.debug(LOG_TAG, "Failed to push token, Connection is null with %s", url);
            return;
        }
        if (connection.getResponseCode() == 200) {
            Log.debug(LOG_TAG, "Successful");
        } else {
            Log.debug(LOG_TAG, "ERROR: %s", connection.getResponseMessage());
        }
    }
}
