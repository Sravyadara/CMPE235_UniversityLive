package com.gimbal.android.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class AdminPage extends Activity {

    String BASE_URL = "https://manager.gimbal.com/api/beacons";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        Button listSensorsButton = (Button) findViewById(R.id.listSensors);
        listSensorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchSensorsList fetchSensorsList = new FetchSensorsList(new AsyncResponse() {
                    @Override
                    public void onProcessFinish(String s) {
                        Intent intent = new Intent(getApplicationContext(), ListSensors.class);
                        intent.putExtra("sensorObject", s);
                        startActivity(intent);
                    }
                });
                fetchSensorsList.execute(BASE_URL);

            }
        });

        Button locateSensorsButton = (Button) findViewById(R.id.locateSensors);
        locateSensorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchSensorsList fetchSensorsList = new FetchSensorsList(new AsyncResponse() {
                    @Override
                    public void onProcessFinish(String s) {
                        Intent intent = new Intent(getApplicationContext(), LocateSensors.class);
                        intent.putExtra("sensorObject", s);
                        startActivity(intent);
                    }
                });
                fetchSensorsList.execute(BASE_URL);

            }
        });

        Button manageSensors = (Button) findViewById(R.id.manageSensors);
        manageSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchSensorsList fetchSensorsList = new FetchSensorsList(new AsyncResponse() {
                    @Override
                    public void onProcessFinish(String s) {
                        Intent intent = new Intent(getApplicationContext(), ManageSensors.class);
                        intent.putExtra("sensorObject", s);
                        startActivity(intent);
                    }
                });
                fetchSensorsList.execute(BASE_URL);

            }
        });

        Button addSensor = (Button) findViewById(R.id.addSensor);
        addSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddSensor.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
