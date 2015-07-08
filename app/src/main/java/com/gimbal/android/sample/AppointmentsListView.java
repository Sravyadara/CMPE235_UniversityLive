package com.gimbal.android.sample;


import java.util.ArrayList;
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
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;


public class AppointmentsListView extends Activity {

    AppointmentsList adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_list_view);
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

                        ParseInstallation.getCurrentInstallation().getInstallationId();
                        System.out.println("installation id: "+ ParseInstallation.getCurrentInstallation().getInstallationId());
                        ParseQuery pushQuery = ParseInstallation.getQuery();
                        ParsePush push = new ParsePush();
                        // xpushQuery.whereEqualTo("user","sravya");
                        pushQuery.whereEqualTo("user","c10a226e-0b07-45fe-9dfb-aea1cad16437");
                        // pushQuery.whereEqualTo("device_id","355451060310295");
                        //Sravya reddy - 356567055976557
                        //Sravya dara mobile - 355451060310295
                        //maruthy mobile - 359691043166571
                        push.setQuery(pushQuery);
                        push.setMessage("Please check appointments!");
                        push.sendInBackground();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }


                Intent intent = new Intent(AppointmentsListView.this, Appointment.class);
                //Create the bundle
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("ListIDcourse", courseIde);

//Add the bundle to the intent
                intent.putExtras(bundle);


                startActivity(intent);

            }
        });
    }

    public ArrayList<AppointmentDetails> GetSearchResults()throws ParseException {
        // Create ItemDetails Arraylist and load all the sensor networks to Arraylist one by one.
        //Then add each item details to Arraylist
        ArrayList<AppointmentDetails> results;
        results= new ArrayList<AppointmentDetails>();


        Availability.availAppointments.size();
        System.out.println(" sizeeee "+ Availability.availAppointments.size());
        for(int i=0;i<Availability.availAppointments.size();i++ ) {
            Student queryStud= new Student();
            Student.getProfessorDetails(Availability.availAppointments.get(i),
                    Availability.sensorID);
            AppointmentDetails Appoint_details = new AppointmentDetails();
            Appoint_details.setProfName(queryStud.profName);
            Appoint_details.setCourseId(queryStud.fetchcourseId);
            Appoint_details.setCourseName(queryStud.courseName);
            Appoint_details.setImageNumber(1);


            results.add(Appoint_details);

        }

        return results;
    }
}