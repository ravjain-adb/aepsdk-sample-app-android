package com.adobe.marketing.mobile.sampleapp;

public class PushConstants {

    // The constant values are for the production IMS Org 'AEM Assets Departmental Org'
    // The Sandbox used is 'CJM End 2 End testing'
    // If you are working in some other Sandbox, please make sure the AEP datasets required are created properly.


    // Step 1
    // The IMSOrg for which the app is being configured
    static final String ORG_ID="906E3A095DC834230A495FD6@AdobeOrg";


    // Step 2
    // The DatasetID of 'CJM Push Profile Dataset' from the specific sandbox for which is the app is being configured.
    // You should find this Dataset in the working sandbox - If the Sandbox is configured properly.
    static final String PUSH_PROFILE_DATASET_ID="601460f70085b8194b6aac02";

    // Step 3
    // The DatasetID of 'CJM Push Tracking Experience Event Dataset' from the specific sandbox for which is the app is being configured.
    // You should find this Dataset in the working sandbox - If the Sandbox is configured properly.
    static final String PUSH_TRACKING_EXPERIENCE_EVENT_DATASET_ID="6014611ed9a0041949769e17";

    // Step 4
    // The DatasetID that's required for this specific app, which allows you to send an experience event
    // The DatasetID of 'AEPSampleApp Experience Event Dataset' from the specific sandbox for which is the app is being configured.
    // This dataset should conform to a schema which is derived from 'XDM Experience Event'.
    // the schema should also have a custom field 'customAction'
    static final String CUSTOM_ACTION_DATASET="60146f1f6fb6ae19481833cb";

    // Step 5
    // The DatasetID of 'AEPSampleApp Extra Profile Dataset' from the specific sandbox for which is the app is being configured.
    // This dataset should conform to any schema which is derived from 'XDM Individual Profile'.
    // Note You cannot reuse the Dataset 'CJM Push Profile Dataset'
    static final String CUSTOM_PROFILE_DATASET="601a5860812a35194ac49baa";

    // Step 6
    // The HTTP source inlet to send updates to Profile
    // You can use an existing HTTP source, make sure it is configured to stream data into 'CJM Push Profile Dataset' and the 'AEPSampleApp Dataset'
    static final String PROFILE_HTTP_DCS_URL="https://dcs.adobedc.net/collection/e28b94f7b68e32480ab10e4880bedb7a51c17b54e2275978cff915ae61f28070";



}


