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


public class CheckURAppointmentsAdapter extends Activity {

    AppointmentsList adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_urappointments_adapter);
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
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(CheckURAppointmentsAdapter.this, WaitingListScreen.class);
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
        Student queryStud= new Student();
        ParseQuery<ParseObject> coursesObj = ParseQuery.getQuery("Appointments");
        coursesObj.whereEqualTo("StudentID", queryStud.userName);
        try{
            List<ParseObject> profdetails = coursesObj.find();
            for (ParseObject eachProf : profdetails) {
               String  profName = eachProf.getString("ProfName");
                String courseId=eachProf.getString("CourseID");
                String courseName = eachProf.getString("CourseName");
                AppointmentDetails Appoint_details = new AppointmentDetails();
                Appoint_details.setProfName(profName);
                Appoint_details.setCourseId(courseId);
                Appoint_details.setCourseName(courseName);
                Appoint_details.setImageNumber(1);
                results.add(Appoint_details);
            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
//        for(int i=0;i<Appointsize;i++ ) {
//            Student queryapp= new Student();
//            int size=queryapp.getAppointmentDetails();
//            AppointmentDetails Appoint_details = new AppointmentDetails();
//            Appoint_details.setProfName(queryStud.profName);
//            Appoint_details.setCourseId(queryStud.fetchcourseId);
//            Appoint_details.setCourseName(queryStud.courseName);
//            Appoint_details.setImageNumber(1);






        return results;
    }
}