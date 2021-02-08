/*
 Copyright 2020 Adobe
 All Rights Reserved.

 NOTICE: Adobe permits you to use, modify, and distribute this file in
 accordance with the terms of the Adobe license agreement accompanying
 it.
 */
package com.adobe.marketing.mobile.sampleapp;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.adobe.marketing.mobile.AdobeCallback;
//import com.adobe.marketing.mobile.Assurance;
import com.adobe.marketing.mobile.Edge;
import com.adobe.marketing.mobile.EdgeCallback;
import com.adobe.marketing.mobile.EdgeEventHandle;
import com.adobe.marketing.mobile.ExperienceEvent;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.SampleAppNetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageTab extends Fragment {

    Button btnUpdateProfile;
    EditText etEmail;
    EditText etFirstName;
    EditText etLastName;
    EditText etFullName;
    EditText etECID;


    private static final String LOG_TAG = "Messaging Tab";

    public MessageTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messaging_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Create references to all our components
        btnUpdateProfile = view.findViewById(R.id.btn_updateProfile);
        etEmail = view.findViewById(R.id.et_emailText);
        etFirstName = view.findViewById(R.id.et_firstNameText);
        etLastName = view.findViewById(R.id.et_lastNameText);
        etFullName = view.findViewById(R.id.et_fullNameText);
        etECID = view.findViewById(R.id.et_ecIDText);


        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

    }


    private void updateProfile() {
        final String profileEmail = etEmail.getText().toString();
        final String profileFirstName = etFirstName.getText().toString();
        final String profileLastName = etLastName.getText().toString();
        final String profileFullName = etFullName.getText().toString();


        Identity.getExperienceCloudId(new AdobeCallback<String>() {
            @Override
            public void call(final String ecid) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        etECID.setText(ecid);
                    }
                });
                final String payload = getProfileUpdatePayload(profileEmail,profileFirstName,profileLastName, profileFullName, ecid);
                Log.d(LOG_TAG, payload);
                SampleAppNetworkConnection connection = new SampleAppNetworkConnection();
                connection.connectPostUrl(MainApp.PLATFORM_DCS_URL, payload.getBytes());
            }
        });
    }

    private String getProfileUpdatePayload(String email, String firstName, String lastName, String fullName, String ecid) {
        return "{\n" +
                "    \"header\" : {\n" +
                "        \"imsOrgId\": \"" + MainApp.ORG_ID + "\",\n" +
                "        \"source\": {\n" +
                "            \"name\": \"mobile\"\n" +
                "        },\n" +
                "        \"datasetId\": \"" + MainApp.CUSTOM_PROFILE_DATASET +"\"\n" +
                "    },\n" +
                "    \"body\": {\n" +
                "        \"xdmEntity\": {\n" +
                "            \"identityMap\": {\n" +
                "                \"ECID\": [\n" +
                "                    {\n" +
                "                        \"id\" : \"" + ecid +"\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"Email\": [\n" +
                "                    {\n" +
                "                        \"id\" : \"" + email +"\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "      \"testProfile\": true,\n" +
                "         \"personalEmail\": {\n" +
                "         \t\"address\": \"" + email +"\"\n" +
                "         },\n" +
                "         \"person\": {\n" +
                "              \"name\": {\n" +
                "              \"firstName\": \"" + firstName + "\",\n" +
                "              \"lastName\": \"" + lastName + "\",\n" +
                "              \"fullName\": \"" + fullName  + "\"\n" +
                "              }\n" +
                "          }\n" +
                "      }\n" +
                "   }\n" +
                "}";
    }
}
