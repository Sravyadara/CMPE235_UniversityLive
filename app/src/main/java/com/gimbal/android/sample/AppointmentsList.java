package com.gimbal.android.sample;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;

import java.util.ArrayList;
import java.util.Locale;


public class AppointmentsList extends BaseAdapter {


//This class extends the BaseAdapter and overrides getView() method to implement custom listview.

    private static ArrayList<AppointmentDetails> itemDetailsrrayList;
    //These are the images that should display for listitems, one for each item.
    private Integer[] imgid = {
            R.drawable.appointments,
            R.drawable.appointments
    };

    private LayoutInflater l_Inflater;

    public AppointmentsList(Context context, ArrayList<AppointmentDetails> results) {
        itemDetailsrrayList = results;
        l_Inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return itemDetailsrrayList.size();
    }

    public Object getItem(int position) {
        return itemDetailsrrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    // To implement BaseAdapter, getView method should be overrided.
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //Retrieving all the sensor network values and placing them in viewHolder object
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.activity_appointments_list, null);
            holder = new ViewHolder();
            holder.txt_ProfName = (TextView) convertView.findViewById(R.id.name);
            holder.txt_CourId = (TextView) convertView.findViewById(R.id.ListcourseId);
            holder.txt_CourName = (TextView) convertView.findViewById(R.id.ListcourseName);
            holder.itemImage = (ImageView) convertView.findViewById(R.id.photo);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_ProfName.setText(itemDetailsrrayList.get(position).getProfName());
        holder.txt_CourId.setText(itemDetailsrrayList.get(position).getCourseId());
        holder.txt_CourName.setText(itemDetailsrrayList.get(position).getCourseName());
        holder.itemImage.setImageResource(imgid[itemDetailsrrayList.get(position).getImageNumber() - 1]);


        return convertView;
    }

    static class ViewHolder {
        TextView txt_ProfName;
        TextView txt_CourId;
        TextView txt_CourName;
        ImageView itemImage;
    }
}
