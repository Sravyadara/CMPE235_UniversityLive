package com.gimbal.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by sravyadara on 4/26/15.
 */
public class Downloads extends Activity {

    // Adapter for the Parse query
    private DownloadsAdapter parseQueryAdapter;
    private ListView downloadsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloads_layout);

       // Bundle extras = getIntent().getExtras();
       // String textvalue= extras.getString("key");
        downloadsList =(ListView)findViewById(R.id.listView);
        parseQueryAdapter = new DownloadsAdapter(this);
        downloadsList.setAdapter(parseQueryAdapter);
        parseQueryAdapter.loadObjects();





    }

}
