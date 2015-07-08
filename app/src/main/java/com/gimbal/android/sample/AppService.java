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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.gimbal.android.Communication;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.CommunicationManager;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Push;
import com.gimbal.android.Push.PushType;
import com.gimbal.android.Visit;
import com.gimbal.android.sample.GimbalEvent.TYPE;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AppService extends Service {
    private static final int MAX_NUM_EVENTS = 100;
    private LinkedList<GimbalEvent> events;
    public static ArrayList<String> entrySensorID;
    public static ArrayList<String> exitSensorID;
    public static ArrayList<Long> arrivalTime;
    public static ArrayList<Long> exitTime;
    private PlaceEventListener placeEventListener;
    private CommunicationListener communicationListener;
    public static String IMEI;


    @Override
    public void onCreate() {
        events = new LinkedList<GimbalEvent>(GimbalDAO.getEvents(getApplicationContext()));
        entrySensorID = new ArrayList<String>();
        exitSensorID = new ArrayList<String>();
        arrivalTime = new ArrayList<Long>();
        exitTime    = new ArrayList<Long>();
      //  Gimbal.setApiKey(this.getApplication(), "9da1beb6-724c-4735-9e0c-e8dbabe0bdb4");

        // Setup PlaceEventListener
        placeEventListener = new PlaceEventListener() {

            @Override
            public void onVisitStart(Visit visit) {
                addEvent(new GimbalEvent(TYPE.PLACE_ENTER, visit.getPlace().getName(), new Date(visit.getArrivalTimeInMillis())));
                entrySensorID.add(visit.getPlace().getName());
                arrivalTime.add(visit.getArrivalTimeInMillis());
                System.out.println(visit.getPlace().getAttributes());
                Student profavailability= new Student();
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                IMEI = telephonyManager.getDeviceId();

                try {
                    profavailability.getStudentDetails(IMEI);
                    profavailability.getProfessorObjectID(profavailability.userName);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String role=profavailability.role;
                if(role.equalsIgnoreCase("Professor"))
                {

                    try {
                        profavailability.getProfessorAvailability("yes");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onVisitEnd(Visit visit) {
                addEvent(new GimbalEvent(TYPE.PLACE_EXIT, visit.getPlace().getName(), new Date(visit.getDepartureTimeInMillis())));
                exitSensorID.add(visit.getPlace().getName());
                exitTime.add(visit.getDepartureTimeInMillis());
                Student profavailability= new Student();
                String role=profavailability.role;
                if(role.equalsIgnoreCase("Professor"))
                {
                    try {
                        profavailability.getProfessorAvailability("no");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        PlaceManager.getInstance().addListener(placeEventListener);

        // Setup CommunicationListener
        communicationListener = new CommunicationListener() {
            @Override
            public Collection<Communication> presentNotificationForCommunications(Collection<Communication> communications, Visit visit) {
                for (Communication comm : communications) {
                    if (visit.getDepartureTimeInMillis() == 0L) {
                        System.out.println("Checking  PresentVisit function");
                       addEvent(new GimbalEvent(TYPE.COMMUNICATION_ENTER, comm.getTitle(), new Date(visit.getArrivalTimeInMillis())));
                    } else {

                        addEvent(new GimbalEvent(TYPE.COMMUNICATION_EXIT, comm.getTitle(), new Date(visit.getDepartureTimeInMillis())));
                    }
                }

                // let the SDK post notifications for the communicates
                return communications;
            }

            @Override
            public Collection<Communication> presentNotificationForCommunications(Collection<Communication> communications, Push push) {
                for (Communication communication : communications) {
                    System.out.println("Inside for loop,communication");
                    if (push.getPushType() == PushType.INSTANT) {
                        System.out.println("Checking Instant eventone");
                        addEvent(new GimbalEvent(TYPE.COMMUNICATION_INSTANT_PUSH, communication.getTitle(), new Date()));
                    } else {
                        System.out.println("Checking add event");
                       addEvent(new GimbalEvent(TYPE.COMMUNICATION_TIME_PUSH, communication.getURL(), new Date()));
                    }
                }

                // let the SDK post notifications for the communicates
                return communications;
            }

            @Override
            public void onNotificationClicked(List<Communication> communications) {

                for (Communication communication : communications) {

                    String url = (String)communication.getURL();
                    System.out.println("MyUrl :" + url);
                    ParseObject urlObject = new ParseObject("URL");
                    urlObject.put("Url",url);
                    urlObject.put("Id","url");

                    urlObject.saveInBackground();
                    Intent i = new Intent(AppService.this,Downloads.class);
                   // i.putExtra("key",url);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(),url,Toast.LENGTH_SHORT).show();

                    addEvent(new GimbalEvent(TYPE.NOTIFICATION_CLICKED, communication.getURL(), new Date()));
                }
            }
        };
            CommunicationManager.getInstance().addListener(communicationListener);
        }



    private void addEvent(GimbalEvent event) {
        while (events.size() >= MAX_NUM_EVENTS) {
            events.removeLast();
        }
        events.add(0, event);
        GimbalDAO.setEvents(getApplicationContext(), events);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        PlaceManager.getInstance().removeListener(placeEventListener);
        CommunicationManager.getInstance().removeListener(communicationListener);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
