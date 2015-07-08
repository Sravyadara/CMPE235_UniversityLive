package com.gimbal.android.sample;

/**
 * Created by Susmitha on 5/9/2015.
 */
public class StudentUser {

    private String title;
    private String desc;
    private String studentId;
    private int thumb;

    public StudentUser(String title, String desc,
                       int thumb, String studentId) {
        this.title = title;
        this.desc = desc;
        this.thumb = thumb;
        this.studentId = studentId;
    }

    public String getTitle() {
        return title;
    }
    public String getDesc() {
        return desc;
    }
    public int getThumb() {
        return thumb;
    }

    public String getStudentId() {
        return studentId;
    }
}
