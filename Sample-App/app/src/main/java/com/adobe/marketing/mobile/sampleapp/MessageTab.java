/*
 Copyright 2020 Adobe
 All Rights Reserved.

 NOTICE: Adobe permits you to use, modify, and distribute this file in
 accordance with the terms of the Adobe license agreement accompanying
 it.
 */
package com.adobe.marketing.mobile.sampleapp;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import org.w3c.dom.Text;

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
    TextView tvECID;

    String ecidValue;


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
        tvECID = view.findViewById(R.id.tv_ecIDText);


        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
                hideKeyboard(getActivity());
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Identity.getExperienceCloudId(new AdobeCallback<String>() {
            @Override
            public void call(final String ecid) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ecidValue = ecid;
                        tvECID.setText(ecidValue);
                    }
                });
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        if (activity == null) return;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void updateProfile() {
        final String profileEmail = etEmail.getText().toString();
        final String profileFirstName = etFirstName.getText().toString();
        final String profileLastName = etLastName.getText().toString();
        final String profileFullName = etFullName.getText().toString();

        HandlerThread ht = new HandlerThread("Bgthread");
        ht.start();
        Looper looper = ht.getLooper();
        Handler h = new Handler(looper);
        h.post(new Runnable() {
            @Override
            public void run() {
                final String payload = getProfileUpdatePayload(profileEmail,profileFirstName,profileLastName, profileFullName, ecidValue);
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
