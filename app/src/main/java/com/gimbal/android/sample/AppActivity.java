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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

public class AppActivity extends Activity {

    private Button eventButton ;
    private Button attendace;
    //  private GimbalEventReceiver gimbalEventReceiver;
    //private GimbalEventListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, AppService.class));

        eventButton =(Button)findViewById(R.id.imageButton);
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AppActivity.this,EventActivity.class);
                startActivity(i);
            }
        });

        Button downloadsButton = (Button) findViewById(R.id.downloadsButton);
        downloadsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Downloads.class);
                startActivity(intent);
            }
        });

       /* startService(new Intent(this, AppService.class));

       if (GimbalDAO.showOptIn(getApplicationContext())) {
            startActivity(new Intent(this, OptInActivity.class));
        }

        adapter = new GimbalEventListAdapter(this);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


               Intent download = new Intent(AppActivity.this,Downloads.class);

                startActivity(download);
            }
        });*/

         /* ---- Code for Admin page starts here with Admin Button */

//        Button adminButton = (Button) findViewById(R.id.adminButton);
//        adminButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), AdminPage.class);
//                startActivity(intent);
//            }
//        });
//
//
//        //--Calling Attendace Activity--
//
//
//          attendace = (Button)findViewById(R.id.attendance);
//          attendace.setOnClickListener(new View.OnClickListener() {
//              @Override
//              public void onClick(View v) {
//                  Intent attendanceIntent = new Intent(getApplicationContext(),Attendance.class);
//                  startActivity(attendanceIntent);
//              }
//          });
//
//
//        Button updateUrlButton = (Button) findViewById(R.id.updateurl);
//        updateUrlButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), NewNotificationURLActivity.class);
//                startActivity(intent);
//            }
//        });
        Button appointments = (Button) findViewById(R.id.appointmentBtn);
        appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ParseInstallation.getCurrentInstallation().saveInBackground();
                ParseInstallation.getCurrentInstallation().getInstallationId();
                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("device_id","356567055976557");
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery);
                push.setMessage(" students are waiting for appointments");
                try {
                    push.send();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), Availability.class);
                startActivity(intent);
            }
        });

        Button chkAppointments = (Button) findViewById(R.id.chkAppointments);
        chkAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CheckURAppointmentsAdapter.class);
                startActivity(intent);
            }
        });
        Button profavailability = (Button) findViewById(R.id.chkAvailability);
        profavailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfAvailabilityListAdapter.class);
                startActivity(intent);
            }
        });

    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        adapter.setEvents(GimbalDAO.getEvents(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        gimbalEventReceiver = new GimbalEventReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GimbalDAO.GIMBAL_NEW_EVENT_ACTION);
        registerReceiver(gimbalEventReceiver, intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(gimbalEventReceiver);
    }

    // --------------------
    // EVENT RECEIVER
    // --------------------

    class GimbalEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //adapter.setEvents(GimbalDAO.getEvents(getApplicationContext()));
        }
    }*/


    // --------------------
    // SETTINGS MENU
    // --------------------





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void logout(View view){
        Session session = new Session(getApplicationContext());
        if(session.getUserDetails() != null){
            session.logoutUser();
            Intent intent = new Intent(this, OptInActivity.class);
            startActivity(intent);
        }
    }

}
