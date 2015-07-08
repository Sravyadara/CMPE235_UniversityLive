package com.gimbal.android.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;

import com.gimbal.android.Gimbal;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Visit;
import com.parse.Parse;
import com.gimbal.android.sample.Student;
import com.parse.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Availability extends Activity {


    public static String IMEI;
    public static String sensorID;
    public static ArrayList<String> availAppointments = new ArrayList<String>();
    public  void setupDetails(String currSensorID)
    {
        if(currSensorID.isEmpty())
        {

            Intent i = new Intent(Availability.this,NoAppointments.class);
            startActivity(i);


        }
        else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEI = telephonyManager.getDeviceId();
            String StudAvailableFrom;
            String StudAvailableTo;
            Student queryStud = new Student();

            try {
                queryStud.getStudentDetails(IMEI);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String role = Student.role;
            if (role.equalsIgnoreCase("Student")) {
                int size = queryStud.listCourses.size();
                for (int i = 0; i < size; i++) {
                    String courseId = queryStud.listCourses.get(i);
                    try {
                        queryStud.getProfessorDetails(courseId, currSensorID);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (queryStud.profName != null) {
                        String studProfName = queryStud.profName;

                        String studCourseName = queryStud.courseName;
                        String StudBeaconID = queryStud.beaconId;
                        StudAvailableFrom = queryStud.availableFrom;
                        StudAvailableTo = queryStud.availableTo;

                        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                        java.sql.Time availableFrom = null;
                        java.sql.Time availableTo = null;
                        java.sql.Time currentTime = null;
                        try {
                            availableFrom = new java.sql.Time(formatter.parse(StudAvailableFrom).getTime());
                            availableTo = new java.sql.Time(formatter.parse(StudAvailableTo).getTime());
                            currentTime = new java.sql.Time(formatter.parse("15:00:00").getTime());
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }

                        if ((availableFrom.getTime() <= currentTime.getTime()) && (currentTime.getTime() <= availableTo.getTime())) {

                            if(!availAppointments.contains(courseId))
                            {
                                availAppointments.add(courseId);
                            }
                            System.out.println("Currenttime is smaller------ " + availableFrom);
                        } else {
                            System.out.println("CurrentTime is greater------ " + currentTime);
                        }
                    }

                }

                if(availAppointments.size() == 0)
                {
                    Intent o = new Intent(Availability.this,NoAppointments.class);
                    startActivity(o);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), AppointmentsListView.class);
                    startActivity(intent);
                }
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);
//       // Gimbal.setApiKey(this.getApplication(), "460cc8d6-b406-4bb0-8efc-55a6866b168f");
        //Parse.initialize(this, "9k8UJH0jwIkxXLEWzDerAvcwdq7NmY0ch6FLXkoy", "Mnf3GCgMFEY83I5E67McRaO3776WrK1xv9f3YEne");

        Long sysTime = System.currentTimeMillis();

        long arrivalTime;
        String entrySensorID;
        if(AppService.arrivalTime.size()>0) {
            arrivalTime = AppService.arrivalTime.get(AppService.arrivalTime.size() - 1);
            entrySensorID = AppService.entrySensorID.get(AppService.entrySensorID.size() - 1);
        }
        else
        {
            arrivalTime   = 0;
            entrySensorID = "BLANK";
        }
        long departTime;
        String exitSensorID;
        if(AppService.exitTime.size() > 0)
        {
            departTime   = AppService.exitTime.get(AppService.exitTime.size()-1);
            exitSensorID = AppService.exitSensorID.get(AppService.exitSensorID.size()-1);
        }
        else
        {
            departTime   = 1;
            exitSensorID = "BLANK";
        }

        if( (entrySensorID.equals(exitSensorID)) && departTime > arrivalTime)
            sensorID = "";
        else if(!(entrySensorID.equals(exitSensorID)) && sysTime >= arrivalTime)
            sensorID = entrySensorID;

        setupDetails(sensorID);


    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_availability, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
