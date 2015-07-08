package com.gimbal.android.sample;




/**
 * Created by Sravya Chitaranjan on 5/7/2015.
 */
public class ProfessorDetails {
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
        return Available;
    }
    public void setCourseName(String Available) {
        this.Available = Available;
    }
    public int getImageNumber() {
        return imageNumber;
    }
    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
    }

    private String name ;
    private String courId;
    private String Available;
    private int imageNumber;


}
