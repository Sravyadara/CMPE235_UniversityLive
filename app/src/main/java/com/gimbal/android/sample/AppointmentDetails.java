package com.gimbal.android.sample;

/**
 * Created by Sravya Chitaranjan on 5/7/2015.
 */
public class AppointmentDetails {
    // This class has the getter setter methods of the sensor network.
    public String getProfName() {
        return name;
    }
    public void setProfName(String name) {
        this.name = name;
    }
    public String getCourseId() {
        return courId;
    }
    public void setCourseId(String courId) {
        this.courId = courId;
    }
    public String getCourseName() {
        return courname;
    }
    public void setCourseName(String courname) {
        this.courname = courname;
    }
    public int getImageNumber() {
        return imageNumber;
    }
    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
    }

    private String name ;
    private String courId;
    private String courname;
    private int imageNumber;


}
