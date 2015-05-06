package com.gimbal.android.sample;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by sravyadara on 5/5/15.
 */
public class DownloadsAdapter extends ParseQueryAdapter {

    public DownloadsAdapter(Context context) {

        super(context, new QueryFactory<ParseObject>() {
            @Override
            public ParseQuery<ParseObject> create() {

                ParseQuery query = new ParseQuery("URL");
                query.whereEqualTo("Id","url");
                query.orderByDescending("createdAt");
                return query;

            }
        });
    }


    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.downloads_item_layout, null);
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
        TextView titleTextView = (TextView) v.findViewById(R.id.downloadsTitle);
        titleTextView.setText(object.getString("Url"));

        // Add a reminder of how long this item has been outstanding
        TextView timestampView = (TextView) v.findViewById(R.id.date);
        timestampView.setText(object.getCreatedAt().toString());
        return v;
    }
}
