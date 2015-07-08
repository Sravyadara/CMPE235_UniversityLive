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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gimbal.android.CommunicationManager;
import com.gimbal.android.Gimbal;
import com.gimbal.android.PlaceManager;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class OptInActivity extends Activity {

    private Context context;
    private String gcmSenderId = "309039147220";
    private String imei;
    private List<String> imeiList ;
    private List<String> userNames;
    public String user;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.optin);

        ParseAnalytics.trackAppOpened(getIntent());
        //Gimbal.setApiKey(this.getApplication(), "9da1beb6-724c-4735-9e0c-e8dbabe0bdb4");
        Gimbal.setApiKey(this.getApplication(), "460cc8d6-b406-4bb0-8efc-55a6866b168f");

        context  = this.getApplicationContext();

        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        imei = mngr.getDeviceId();
//        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//        installation.put("device_id", imei);
//        try {
//            installation.save();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

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
        Button signup = (Button) findViewById(R.id.btn_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserRegistration.class);
                startActivity(intent);
            }
        });
    }

    public void onEnableClicked(View view) throws ParseException {

        UserVO uservo = new UserVO();
        String errorMessage = null;
        session = new Session(getApplicationContext());

        GimbalDAO.setOptInShown(getApplicationContext());
       PlaceManager.getInstance().startMonitoring();

        TextView txtView_error = (TextView) findViewById(R.id.txtv_error);
        EditText userName =  (EditText)findViewById(R.id.txt_UN);
        EditText password =  (EditText)findViewById(R.id.txt_password);
        List<ParseObject> userObjects = new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Registration");
        query.whereEqualTo("User_Name", userName.getText().toString());

        userObjects = query.find();
        for(ParseObject user : userObjects){

            if(user.getString("Password").equals(password.getText().toString())) {
                uservo.setFirstName(user.getString("First_Name"));
                uservo.setLastName(user.getString("Last_Name"));
                uservo.setEmailId(user.getString("Email_Id"));
                uservo.setPhoneNumber(user.getString("Phone_Number"));
                uservo.setAddress(user.getString("Address"));
                uservo.setIMEI(user.getString("IMEI"));
                uservo.setRole(user.getString("Role"));

                //Check for student
                System.out.println("IMEIList:"+imeiList);
                session.createLoginSession(userName.getText().toString(),uservo.getEmailId());
               // ParseInstallation.getCurrentInstallation().put("name", userName.getText().toString());
              //  ParseInstallation.getCurrentInstallation().put("phonenumber", uservo.getPhoneNumber());
                //ParseInstallation.getCurrentInstallation().saveInBackground();

            }
            else{
                errorMessage = "Invalid UserName or Password";
                txtView_error.setText(errorMessage);
                Intent intent = new Intent(this, OptInActivity.class);
                startActivity(intent);
            }

        }
        if(uservo!= null){
            if(uservo.getRole().equals("Admin")){
                System.out.println("Admin");
                //redirect to Admin Activity
                Intent intent = new Intent(this, AdminPage.class);
                startActivity(intent);
            }else if(uservo.getRole().equals("Student")){
                if(imeiList.size() > 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = Calendar.getInstance().getTime();
                    String  dateNoTime = sdf.format(date);

                    //Storing the Student Name in Parse
                    ParseObject studentAttendance = new ParseObject("Attendance");
                    studentAttendance.put("StudentName",uservo.getFirstName()+uservo.getLastName());
                    studentAttendance.put("Date",sdf.format(date));
                    System.out.println("SystemDate:"+dateNoTime);
                    studentAttendance.put("Attendance",true);
                    studentAttendance.saveInBackground();
                    CommunicationManager.getInstance().startReceivingCommunications();
                }

                //redirect to Student Activity
                Intent intent = new Intent(this, AppActivity.class);
                startActivity(intent);

                registerForPush(gcmSenderId);
                finish();
            }else{
                System.out.println("Professor");
                //redirect to Professor Activity
                Intent intent = new Intent(this, ProfessorActivity  .class);
                startActivity(intent);
            }
        }
        /*Intent mainIntent = new Intent(OptInActivity.this,AppActivity.class);
        startActivity(mainIntent);
        registerForPush(gcmSenderId);
            finish();
*/

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