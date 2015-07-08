package com.gimbal.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Appointments extends Activity {

    private ListView lv;
    ArrayList<StudentUser> studentUserArray = new ArrayList<StudentUser>();
    VersionAdapter versionAdapter;
    static TextView waitCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        Session session = new Session(getApplicationContext());
        HashMap<String,String> usersession = session.getUserDetails();

        System.out.println("--------------------Usersession: "+usersession.get("name"));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Appointments");
        query.whereEqualTo("ProfID",usersession.get("name"));
        List<ParseObject> waitingStudents = new ArrayList<ParseObject>();
        try {
            waitingStudents = query.find();
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        for(ParseObject student : waitingStudents){
            StudentUser sn = new StudentUser(student.getString("StudentName"),student.getString("CourseID"),R.drawable.graduatemale, student.getString("StudentID"));
            studentUserArray.add(sn);
        }
        /*for (int i = 0; i < title.length; i++) {
            Student sn = new Student(title[i], desc[i], thumb[i]);
            // Binds all strings into an array
            studentArray.add(sn);
        }*/
        // initialize the variables:

        waitCount = (TextView) findViewById(R.id.txt_waitcount);
        waitCount.setText("No. of students waiting: "+Integer.toString(studentUserArray.size()));
        versionAdapter = new VersionAdapter(Appointments.this, R.layout.single_row,
                studentUserArray);
        lv = (ListView) findViewById(R.id.appointments);
        lv.setItemsCanFocus(false);
        lv.setAdapter(versionAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.i("List View Clicked", "**********");
                Toast.makeText(Appointments.this,
                        "List View Clicked:" + position, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }



}
