package com.gimbal.android.sample;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sravyadara on 5/6/15.
 */
public class AttendanceAdapter extends ParseQueryAdapter {

    public AttendanceAdapter(Context context) {

        super(context, new QueryFactory<ParseObject>() {
            @Override
            public ParseQuery<ParseObject> create() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();

                ParseQuery query = new ParseQuery("Attendance");
               // query.whereEqualTo("Attendance",true);
                query.whereEqualTo("Date", sdf.format(date));
                return query;

            }
        });
    }


    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.attendance_list_item_layout, null);
        }

        super.getItemView(object, v, parent);

        // Add and download the image
        /*ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.icon);
        ParseFile imageFile = object.getParseFile("image");
        if (imageFile != null) {
            todoImage.setParseFile(imageFile);
            todoImage.loadInBackground();
        }*/

        // Add the title view
        TextView nameTextView = (TextView) v.findViewById(R.id.student_name);
        nameTextView.setText(object.getString("StudentName"));


        return v;
    }


}
