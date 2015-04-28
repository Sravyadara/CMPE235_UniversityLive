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

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.gimbal.android.Communication;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.CommunicationManager;
import com.gimbal.android.Gimbal;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Push;
import com.gimbal.android.Push.PushType;
import com.gimbal.android.Visit;
import com.gimbal.android.sample.GimbalEvent.TYPE;
import android.content.Context;

public class AppService extends Service {
    private static final int MAX_NUM_EVENTS = 100;
    private LinkedList<GimbalEvent> events;
    private PlaceEventListener placeEventListener;
    private CommunicationListener communicationListener;

    @Override
    public void onCreate() {
        events = new LinkedList<GimbalEvent>(GimbalDAO.getEvents(getApplicationContext()));

      //  Gimbal.setApiKey(this.getApplication(), "9da1beb6-724c-4735-9e0c-e8dbabe0bdb4");

        // Setup PlaceEventListener
        placeEventListener = new PlaceEventListener() {

            @Override
            public void onVisitStart(Visit visit) {
                addEvent(new GimbalEvent(TYPE.PLACE_ENTER, visit.getPlace().getName(), new Date(visit.getArrivalTimeInMillis())));
                System.out.println(visit.getPlace().getAttributes());
            }

            @Override
            public void onVisitEnd(Visit visit) {
                addEvent(new GimbalEvent(TYPE.PLACE_EXIT, visit.getPlace().getName(), new Date(visit.getDepartureTimeInMillis())));
            }
        };
        PlaceManager.getInstance().addListener(placeEventListener);

        // Setup CommunicationListener
        communicationListener = new CommunicationListener() {
            @Override
            public Collection<Communication> presentNotificationForCommunications(Collection<Communication> communications, Visit visit) {
                for (Communication comm : communications) {
                    if (visit.getDepartureTimeInMillis() == 0L) {
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
                    if (push.getPushType() == PushType.INSTANT) {
                        addEvent(new GimbalEvent(TYPE.COMMUNICATION_INSTANT_PUSH, communication.getTitle(), new Date()));
                    } else {
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
                    Intent i = new Intent(AppService.this,Downloads.class);
                    i.putExtra("key",url);
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