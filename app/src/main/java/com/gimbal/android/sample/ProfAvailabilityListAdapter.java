package com.gimbal.android.sample;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class ProfAvailabilityListAdapter extends Activity {

    AppointmentsList adapter;

    public static String IMEI;
    public static String sensorID;
    public static ArrayList<String> availAppointments = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_availability_list_adapter);
        // The available sensor networks are placed in an ArrayList
        ArrayList<AppointmentDetails> image_details = null;
        try {
            image_details = GetSearchResults();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Adding them to the adapter.
        adapter = null;
        adapter=    new AppointmentsList(this,image_details);

        // Setting the images for the custom ListView.
        final ListView lv1 = (ListView) findViewById(R.id.listV_main);
        lv1.setAdapter(new AppointmentsList(this, image_details));
        //User is redirected to sensors list page by clicking on each sensor network item.
        lv1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                String courseIde="";
                try {
                    ArrayList<AppointmentDetails> srchDet = GetSearchResults();


                    //System.out.println("Student name"+ Availability.sensorID);
                    Student stud = new Student();

                    try {
                        courseIde = srchDet.get(((int) id)).getCourseId();
                        stud.getProfessorDetails(courseIde, Availability.sensorID);
                        stud.createAppointment();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }


//                Intent intent = new Intent(ProfAvailabilityListAdapter.this, Appointment.class);
//                //Create the bundle
//                Bundle bundle = new Bundle();
//
////Add your data to bundle
//                bundle.putString("ListIDcourse", courseIde);
//
////Add the bundle to the intent
//                intent.putExtras(bundle);
//
//
//                startActivity(intent);

            }
        });
    }

    public ArrayList<AppointmentDetails> GetSearchResults()throws ParseException {
        // Create ItemDetails Arraylist and load all the sensor networks to Arraylist one by one.
        //Then add each item details to Arraylist
        ArrayList<AppointmentDetails> results;
        results = new ArrayList<AppointmentDetails>();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();
        Student queryStud = new Student();

        try {
            queryStud.listCourses.clear();
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
                queryStud.getProfAvailability(courseId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (queryStud.profName != null) {
                String studProfName = queryStud.profName;
                String studCourseName = queryStud.courseName;
                String available=queryStud.available;
                if(available.equalsIgnoreCase("no"))
                {
                    available="Professor is not available";

                }
                else if(available.equalsIgnoreCase("yes")){
                    available="Professor is available";
                }

                AppointmentDetails Appoint_details = new AppointmentDetails();
                Appoint_details.setProfName(studProfName);
                Appoint_details.setCourseId(studCourseName);
                Appoint_details.setCourseName(available);
                Appoint_details.setImageNumber(1);
                results.add(Appoint_details);
            }
        }
        }
        return results;
    }
}