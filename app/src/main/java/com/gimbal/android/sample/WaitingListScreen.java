package com.gimbal.android.sample;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;


public class WaitingListScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list_screen);
        Bundle bundle = getIntent().getExtras();
        String ListIDcourse = bundle.getString("ListIDcourse");
        Student stu= new Student();
        try {
            stu.getProfessorDetails(ListIDcourse,Availability.sensorID);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        System.out.println("ListItem value in Appointment class:"+ListIDcourse);


        int appointmentsCount=0;
        try {
            int flag=1;
           // appointmentsCount=stu.getAppointmentsCount(ListIDcourse);
          appointmentsCount=stu.getAppointmentsCount(ListIDcourse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String converttostr= Integer.toString(appointmentsCount);
        String currentCount= "You are the "+converttostr+" member in the queue";
        TextView tvName = (TextView)findViewById(R.id.listTxt);
        tvName.setText(currentCount);
        int waitingLit =(appointmentsCount-1)*5;
        String convertwaitinglit= Integer.toString(waitingLit);
        EditText profname = (EditText)findViewById(R.id.editText);
        profname.setText(stu.profName, TextView.BufferType.EDITABLE);
        EditText profroomno = (EditText)findViewById(R.id.editText2);
        profroomno.setText(stu.profRoomNo, TextView.BufferType.EDITABLE);
        EditText courseid = (EditText)findViewById(R.id.editText3);
        courseid.setText(stu.courseId, TextView.BufferType.EDITABLE);
        EditText coursename = (EditText)findViewById(R.id.editText4);
        coursename.setText(stu.courseName, TextView.BufferType.EDITABLE);
        EditText waitingtime = (EditText)findViewById(R.id.editText5);
        waitingtime.setText(convertwaitinglit+" mins", TextView.BufferType.EDITABLE);
        System.out.println("Appointments Count:"+convertwaitinglit);

        Button homeBtn= (Button)findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WaitingListScreen.this,AppActivity.class);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointment, menu);
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
