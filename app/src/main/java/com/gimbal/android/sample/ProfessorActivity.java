package com.gimbal.android.sample;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import com.parse.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ProfessorActivity extends Activity {

    private Button attendance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        Button updateUrlButton = (Button) findViewById(R.id.updateurl);
        updateUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewNotificationURLActivity.class);
                startActivity(intent);
            }
        });

        Button attendance = (Button)findViewById(R.id.btn_attendance);
          attendance.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent attendanceIntent = new Intent(getApplicationContext(),Attendance.class);
                  startActivity(attendanceIntent);
              }
          });
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

    public int studentsWaiting(){

        String noOfStudentsWaiting = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Appointments");
        List<ParseObject> waitingStudents = new ArrayList<ParseObject>();
        UserVO uservo = new UserVO();
        ArrayList<UserVO> studentsList = new ArrayList<UserVO>();
        try {
            waitingStudents = query.find();
        }catch(ParseException pe){
            pe.printStackTrace();
        }

        for(ParseObject student : waitingStudents){
            uservo.setFirstName(student.getString("First_Name"));
            uservo.setLastName(student.getString("Last_Name"));
            uservo.setEmailId(student.getString("Email_Id"));
            uservo.setPhoneNumber(student.getString("Phone_Number"));
            uservo.setAddress(student.getString("Address"));
            uservo.setIMEI(student.getString("IMEI"));
            uservo.setRole(student.getString("role"));
            studentsList.add(uservo);
        }

        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush push = new ParsePush();
        push.setMessage(waitingStudents.size()+ " students are waiting for appointments");
        push.sendInBackground();

        return 0;
    }

    public int appointmentDone(String ID) throws ParseException{

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Appointments");
        query.whereEqualTo("ID", ID);
        List<ParseObject> completedStudents = new ArrayList<ParseObject>();
        completedStudents = query.find();
        ParseObject userCompleted = completedStudents.get(0);
        userCompleted.delete();

        return 0;
    }
    public void showlist(View view){
        Intent intent = new Intent(this, Appointments.class);
        startActivity(intent);
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
