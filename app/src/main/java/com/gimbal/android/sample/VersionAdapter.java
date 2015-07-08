package com.gimbal.android.sample;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Susmitha on 5/9/2015.
 */
class VersionAdapter extends ArrayAdapter<StudentUser> {

    static class ViewHolder {
        TextView studentName;
        TextView course;
        Button btnDelete;
        TextView studentID;
        ImageView thumb;
    }


    Context context;
    int layoutResourceId;
    ArrayList<StudentUser> data = new ArrayList<StudentUser>();

    public VersionAdapter(Context context, int layoutResourceId,
                          ArrayList<StudentUser> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public StudentUser getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.studentName = (TextView) row.findViewById(R.id.studentname);
            holder.course = (TextView) row.findViewById(R.id.course);
            holder.thumb = (ImageView) row.findViewById(R.id.thumb);
            holder.studentID = (TextView) row.findViewById(R.id.studentID);
            holder.btnDelete = (Button) row.findViewById(R.id.btn_delete);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        StudentUser user = data.get(position);
        holder.studentName.setText(user.getTitle());
        holder.course.setText(user.getDesc());
        holder.thumb.setImageResource(user.getThumb());
        holder.studentID.setText(user.getStudentId());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Delete Button Clicked", "**********");
              /*  Toast.makeText(context, "Delete button Clicked",
                        Toast.LENGTH_LONG).show();*/
                StudentUser user1 = data.get(position);
                try{
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Appointments");
                    query.whereEqualTo("StudentID", user1.getStudentId());
                    List<ParseObject> completedStudents = query.find();
                    ParseObject userCompleted = completedStudents.get(0);
                    userCompleted.delete();

                    data.remove(position);
                    Appointments.waitCount.setText("No. of students waiting: "+ Integer.toString(data.size()));

                    //parse push
                    ParseInstallation.getCurrentInstallation().getInstallationId();
                    System.out.println("installation id: "+ ParseInstallation.getCurrentInstallation().getInstallationId());
                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    ParsePush push = new ParsePush();
                    // xpushQuery.whereEqualTo("user","sravya");
                    pushQuery.whereEqualTo("user","99ad7c52-0986-4d00-a196-1f3298400a80");
                    // pushQuery.whereEqualTo("device_id","355451060310295");
                    //Sravya reddy - 356567055976557
                    //Sravya dara mobile - 355451060310295
                    //maruthy mobile - 359691043166571
                    push.setQuery(pushQuery);
                    push.setMessage("You are next!");
                    push.sendInBackground();



                    notifyDataSetChanged();
                }
                catch(ParseException e){
                    e.printStackTrace();
                }
            }
        });
        return row;

    }

    }