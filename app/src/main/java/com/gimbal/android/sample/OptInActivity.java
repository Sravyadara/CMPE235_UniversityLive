/**
 * Copyright (C) 2014 Gimbal, Inc. All rights reserved.
 *
 * This software is the confidential and proprietary information of Gimbal, Inc.
 *
 * The following sample code illustrates various aspects of the Gimbal SDK.
 *
 * The sample code herein is provided for your convenience, and has not been
 * tested or designed to work on any particular system configuration. It is
 * provided AS IS and your use of this sample code, whether as provided or
 * with any modification, is at your own risk. Neither Gimbal, Inc.
 * nor any affiliate takes any liability nor responsibility with respect
 * to the sample code, and disclaims all warranties, express and
 * implied, including without limitation warranties on merchantability,
 * fitness for a specified purpose, and against infringement.
 */
package com.gimbal.android.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;

import com.gimbal.android.CommunicationManager;
import com.gimbal.android.Gimbal;
import com.gimbal.android.PlaceManager;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class OptInActivity extends Activity {

    private Context context;
    private String gcmSenderId = "309039147220";
    private String imei;
    private List<String> imeiList ;
    private List<String> userNames;
    public String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.optin);
      //Gimbal.setApiKey(this.getApplication(), "9da1beb6-724c-4735-9e0c-e8dbabe0bdb4");
        context  = this.getApplicationContext();

        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        imei = mngr.getDeviceId();

        ParseQuery<RegistrationDAO> query = ParseQuery.getQuery("Registration");
        query.whereEqualTo("Role", "Student");
        imeiList = new ArrayList<String>();
        userNames = new ArrayList<String>();

        query.findInBackground(new FindCallback<RegistrationDAO>() {
            @Override
            public void done(List<RegistrationDAO> registrationDAOs, com.parse.ParseException e) {
                for (RegistrationDAO reg : registrationDAOs) {
                    if (imei.equals(reg.getImei())) {
                        System.out.println("IMEI value :" +reg.get("IMEI"));
                        imeiList.add(reg.getImei());
                        userNames.add(reg.getUserName());
                         user = userNames.get(0);

                      /*  //Storing the Student Name in Parse
                        ParseObject studentAttendance = new ParseObject("Attendance");
                        studentAttendance.put("StudentName",user);
                        studentAttendance.put("Attendance",true);
                        studentAttendance.saveInBackground();*/


                        break;
                    }
                }
            }
        });
    }

    public void onEnableClicked(View view) {
        GimbalDAO.setOptInShown(getApplicationContext());

        PlaceManager.getInstance().startMonitoring();
        //Check for student
        System.out.println("IMEIList:"+imeiList);
        if(imeiList.size() >0) {

            //Storing the Student Name in Parse
            ParseObject studentAttendance = new ParseObject("Attendance");
            studentAttendance.put("StudentName",user);
            studentAttendance.put("Attendance",true);
            studentAttendance.saveInBackground();
            CommunicationManager.getInstance().startReceivingCommunications();


        }
        Intent mainIntent = new Intent(OptInActivity.this,AppActivity.class);
        startActivity(mainIntent);
        registerForPush(gcmSenderId);
            finish();


    }

    private void registerForPush(String gcmSenderId) {
        if (gcmSenderId != null) {
            Gimbal.registerForPush(gcmSenderId);
        }
    }

    public void onNotNowClicked(View view) {
        GimbalDAO.setOptInShown(getApplicationContext());
        PlaceManager.getInstance().stopMonitoring();
        finish();
    }



    public void onPrivacyPolicyClicked(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://your-privacy-policy-url")));
    }

    public void onTermsOfServiceClicked(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://your-terms-of-use-url")));
    }
}
