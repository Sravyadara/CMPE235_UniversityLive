package com.gimbal.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by sravyadara on 4/26/15.
 */
public class Downloads extends Activity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloads_layout);

        Bundle extras = getIntent().getExtras();
        String textvalue= extras.getString("key");

        textView = (TextView)findViewById(R.id.textView1);
        textView.setText(textvalue);

    }

}
