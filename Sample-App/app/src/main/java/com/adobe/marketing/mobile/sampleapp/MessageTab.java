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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.Assurance;
import com.adobe.marketing.mobile.Edge;
import com.adobe.marketing.mobile.EdgeCallback;
import com.adobe.marketing.mobile.EdgeEventHandle;
import com.adobe.marketing.mobile.ExperienceEvent;
import com.adobe.marketing.mobile.Identity;
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
    EditText etProfileName;
    Button btnSend;
    EditText etCustomEvent;
    TextView tvECID;

    private static final String LOG_TAG = "Assurance Tab";

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
        btnSend = view.findViewById(R.id.btn_sendEvent);
        btnUpdateProfile = view.findViewById(R.id.btn_updateProfile);
        etCustomEvent = view.findViewById(R.id.et_customEvent);
        etProfileName = view.findViewById(R.id.et_profileData);
        tvECID = view.findViewById(R.id.tv_ecIDText);


        //Setup button events
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCustomExperienceEvent();
                hideKeyboard(getActivity());
            }
        });

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
                        String ecidValue = ecid;
                        tvECID.setText(ecidValue);
                    }
                });
            }
        });
    }

    private void sendCustomExperienceEvent() {
        String customActionValue = etCustomEvent.getText().toString();

        Map<String, Object> xdm = new HashMap<>();
        xdm.put("eventType", "customAction");
        Map<String, Object> aemMap = new HashMap<>();
        aemMap.put("custom_action", customActionValue);
        xdm.put("_aemonacpprodcampaign", aemMap);

        if (!customActionValue.isEmpty()) {
            ExperienceEvent event = new ExperienceEvent.Builder().setData(null).setXdmSchema(xdm, MainApp.CUSTOM_ACTION_DATASET).build();
            Edge.sendEvent(event, new EdgeCallback() {
                @Override
                public void onComplete(List<EdgeEventHandle> list) {
                    Log.d(LOG_TAG, "onComplete");
                }
            });
        }
    }

    private void updateProfile() {
        final String profileName = etProfileName.getText().toString();
        Identity.getExperienceCloudId(new AdobeCallback<String>() {
            @Override
            public void call(String s) {
                final String payload = getProfileUpdatePayload(profileName, s);
                SampleAppNetworkConnection connection = new SampleAppNetworkConnection();
                connection.connectPostUrl(MainApp.PLATFORM_DCS_URL, payload.getBytes());
            }
        });
    }

    private String getProfileUpdatePayload(String profileName, String ecid) {
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
                "                        \"id\" : \"" + profileName +"\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "      \"testProfile\": true,\n" +
                "         \"personalEmail\": {\n" +
                "         \t\"address\": \"" + profileName +"\"\n" +
                "       }\n" +
                "      }\n" +
                "   }\n" +
                "}";
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
}
