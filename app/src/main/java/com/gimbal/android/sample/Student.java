package com.gimbal.android.sample;

import android.app.Application;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sravya Chitaranjan on 5/5/2015.
 */
public class Student extends Application {
    public static String firstName;
    public static String lastName;
    public static String emailID;
    public static String phoneNo;
    public static String address;
    public static String userName;
    public static String password;
    public static String IMEI;
    public static String role;

    public static String profId;
    public static String profName;
    public static String beaconId;
    public static String profRoomNo;
    public static String courseId;
    public static String courseName;
    public static String availableFrom;
    public static String availableTo;
    public static String available;
    public static String fetchcourseId;
    public static String regobjectId;
    public static String regprofobjectId;
    public static ArrayList<String> listCourses = new ArrayList<String>();

    @Override
    public void onCreate() {
       // Parse.initialize(this, "9k8UJH0jwIkxXLEWzDerAvcwdq7NmY0ch6FLXkoy", "Mnf3GCgMFEY83I5E67McRaO3776WrK1xv9f3YEne");
        try {
            getStudentDetails(IMEI);
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }


    }
   public static void getStudentDetails(String queryIMEI)throws com.parse.ParseException
   {
       ParseQuery<ParseObject> query = ParseQuery.getQuery("Registration");
       query.whereEqualTo("IMEI", queryIMEI);
       List<ParseObject> rec = query.find();

       //  listOfCourses.addAll(rec);
       for (ParseObject studentdetails : rec) {


           firstName = studentdetails.getString("First_Name");
           lastName = studentdetails.getString("Last_Name");
           emailID = studentdetails.getString("Email_Id");
           phoneNo = studentdetails.getString("Phone_Number");
           address = studentdetails.getString("Address");
           userName = studentdetails.getString("User_Name");
           password = studentdetails.getString("Password");
           IMEI = studentdetails.getString("IMEI");
           role = studentdetails.getString("Role");
           //regobjectId = studentdetails.getObjectId();
           try {
               getStudentCourses(userName);
           } catch (com.parse.ParseException e1) {
               e1.printStackTrace();
           }
       }
               }


    public static void getStudentCourses(String uName)throws com.parse.ParseException{

        ParseQuery<ParseObject> coursesObj = ParseQuery.getQuery("StudentCourses");
        coursesObj.whereEqualTo("StudentID", uName);
        try {
            List<ParseObject> rec = coursesObj.find();

          //  listOfCourses.addAll(rec);
            for (ParseObject listDetails : rec) {
                courseId = listDetails.get("CourseID").toString();
                listCourses.add(courseId);
                System.out.println("Courses------ "+  courseId);
                //getProfessorDetails(courseId);

            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }


    }
    public static void getProfessorDetails(String courseIdl,String sensorID)throws com.parse.ParseException{

        ParseQuery<ParseObject> coursesObj = ParseQuery.getQuery("Professors");
        coursesObj.whereEqualTo("CourseID", courseIdl);
        coursesObj.whereEqualTo("BeaconID",sensorID);

        try {
            List<ParseObject> profdetails = coursesObj.find();

            for (ParseObject eachProf : profdetails) {
                profId = eachProf.getString("ProfId");
                profName = eachProf.getString("ProfName");
                beaconId = eachProf.getString("BeaconID");
                fetchcourseId=eachProf.getString("CourseID");
                profRoomNo = eachProf.getString("ProfRoomNo");
                courseName = eachProf.getString("CourseName");
                courseId = eachProf.getString("CourseID");
                availableFrom = eachProf.getString("AvailableFrom");
                availableTo = eachProf.getString("AvailableTo");
                available= eachProf.getString("Available");
                regobjectId = eachProf.getObjectId();
                System.out.println("AvailableFrom------ "+  availableFrom);
//                DateFormat formatter = new SimpleDateFormat("HH:mm");
//                java.sql.Time availableFrom = new java.sql.Time(formatter.parse(availFrom).getTime());
//                java.sql.Time availableTo = new java.sql.Time(formatter.parse(availTo).getTime());
//                if(availableFrom.getTime()> availableTo.getTime())
//                {
//                    System.out.println("timeValue------ "+  availableFrom);
//                }
//
//                System.out.println("Noway--------timeValue------ "+  availableTo);

            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }


    }
    public static void getProfAvailability(String courseIdl)throws com.parse.ParseException{

        ParseQuery<ParseObject> coursesObj = ParseQuery.getQuery("Professors");
        coursesObj.whereEqualTo("CourseID", courseIdl);

        try {
            List<ParseObject> profdetails = coursesObj.find();

            for (ParseObject eachProf : profdetails) {
                profId = eachProf.getString("ProfId");
                profName = eachProf.getString("ProfName");
                courseName = eachProf.getString("CourseName");
                courseId = eachProf.getString("CourseID");
                available= eachProf.getString("Available");
                regobjectId = eachProf.getObjectId();
             }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }


    }
    public static void getProfessorObjectID(String profid)throws com.parse.ParseException{

        ParseQuery<ParseObject> coursesObj = ParseQuery.getQuery("Professors");
        coursesObj.whereEqualTo("ProfId", profid);

        try {
            List<ParseObject> profdetails = coursesObj.find();

            for (ParseObject eachProf : profdetails) {

                regprofobjectId = eachProf.getObjectId();
                System.out.println("objectId ------"+ regprofobjectId);

            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }


    }
    public static void createAppointment()throws com.parse.ParseException
    {
        //Storing the Student Name in Parse
        ParseObject appointments = new ParseObject("Appointments");
        appointments.put("StudentID",userName);
        appointments.put("StudentName",firstName);
        appointments.put("ProfID",profId);
        appointments.put("ProfName",profName);
        appointments.put("ProfRoomNo",profRoomNo);
        appointments.put("CourseID",courseId);
        appointments.put("CourseName", courseName);



        try {
            appointments.save();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
    }
    public static int getAppointmentsCount(String AppointcouID)throws com.parse.ParseException{
        int count=0;
        ParseQuery<ParseObject> coursesObj = ParseQuery.getQuery("Appointments");
        coursesObj.whereEqualTo("CourseID",courseId);
        coursesObj.orderByDescending("createdAt");
        //coursesObj.addAscendingOrder("createdAt");

        try {
            List<ParseObject> rec = coursesObj.find();


            for (ParseObject listDetails : rec) {

                count++;


            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return count;

    }
    public static int getWaitinglistCount(String AppointcouID)throws com.parse.ParseException{
        int count=0;
        ParseQuery<ParseObject> coursesObj = ParseQuery.getQuery("Appointments");
        coursesObj.whereEqualTo("CourseID",courseId);
        coursesObj.orderByDescending("createdAt");
        //coursesObj.addAscendingOrder("createdAt");

        try {
            List<ParseObject> rec = coursesObj.find();


            for (ParseObject listDetails : rec) {

                 count++;
                      String usNmae  = listDetails.getString("StudentID");
                if(usNmae.equalsIgnoreCase(userName))
                 {
                   break;
                }

            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return count;

    }
    public static int getAppointmentDetails()throws com.parse.ParseException{
        int appcount=0;
         ParseQuery<ParseObject> coursesObj = ParseQuery.getQuery("Appointments");
        coursesObj.whereEqualTo("StudentID", userName);
        try{
        List<ParseObject> profdetails = coursesObj.find();
        for (ParseObject eachProf : profdetails) {
            appcount++;
            profName = eachProf.getString("ProfName");
            courseId=eachProf.getString("CourseID");
            courseName = eachProf.getString("CourseName");
            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return appcount;

    }
    public static void getProfessorAvailability(final String profAvailablility)throws com.parse.ParseException {
        int count = 0;
        ParseQuery<ParseObject> profavail = ParseQuery.getQuery("Professors");
       profavail.whereEqualTo("objectId", regprofobjectId);
      // profavail.find();

        ParseObject newobject  = profavail.find().get(0);
        System.out.println("BeaconID: "+ newobject.getString("BeaconID"));
        if(newobject != null) {
            newobject.put("Available", profAvailablility);
            newobject.save();
        }
       // profavail.get(regobjectId);


                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.



        }
    }



