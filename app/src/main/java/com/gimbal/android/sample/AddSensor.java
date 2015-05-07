package com.gimbal.android.sample;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;


public class AddSensor extends Activity {

    private static EditText sensorFactoryId;
    private String floorNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        Spinner floorSpinner = (Spinner) findViewById(R.id.floorSpinner);
        ArrayList<String> floors = new ArrayList<String>();
        floors.add("Floor 1");
        floors.add("Floor 2");
        floors.add("Floor 3");
        floors.add("Floor 4");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, floors);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorSpinner.setAdapter(arrayAdapter);

        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                floorNumber = parent.getItemAtPosition(position).toString().split(" ")[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            ImageButton scanButton = (ImageButton) findViewById(R.id.scan);
            scanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_FORMATS", "DATA_MATRIX");
                    startActivityForResult(intent, 0);

                }
            });

        }catch (ActivityNotFoundException anfe) {
            Log.e("onCreate", "Scanner not found", anfe);
        }

        final EditText sensorName = (EditText) findViewById(R.id.sensorNameValue);
        final EditText latitude = (EditText) findViewById(R.id.sensorLatitudeValue);
        final EditText longitude = (EditText) findViewById(R.id.sensorLongitudeValue);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        Button submitButton = (Button) findViewById(R.id.submitButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);

        final String BASE_URL = "https://manager.gimbal.com/api/beacons";

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Inside submit click event");
                if(sensorName.getText().toString().length() == 0) {
                    sensorName.setError(Html.fromHtml("<font color='green'> Name can not be null </font>"));
                }else if(latitude.getText().toString().length() == 0) {
                    latitude.setError(Html.fromHtml("<font color='green'> Latitude can not be null </font>"));
                }else if(longitude.getText().toString().length() == 0) {
                    longitude.setError(Html.fromHtml("<font color='green'> Longitude can not be null </font>"));
                }else {
                    UpdateSesnor updateSesnor = new UpdateSesnor(new AsyncResponse() {
                        @Override
                        public void onProcessFinish(String s) {
                            try {
                                JSONObject responseJsonObject = new JSONObject(s);
                                System.out.println("Printing PUT request Response in Add Sensor Activity : " + responseJsonObject);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    try {
                        JSONObject requestObject = new JSONObject();
                        String name = sensorName.getText().toString() + "_F" + floorNumber;
                        requestObject.put("name", name);
                        requestObject.put("factory_id", sensorFactoryId.getText().toString());
                        requestObject.put("latitude", Double.parseDouble(latitude.getText().toString()));
                        requestObject.put("longitude", Double.parseDouble(longitude.getText().toString()));

                        int checkedMode = radioGroup.getCheckedRadioButtonId();
                        final RadioButton visibilityMode = (RadioButton) findViewById(checkedMode);
                        requestObject.put("visibility", visibilityMode.getText().toString());

                        updateSesnor.execute(BASE_URL, requestObject.toString(), "1");

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
                sensorFactoryId = (EditText) findViewById(R.id.sensorFactoryIdValue);
                String value = contents.substring(0,4) + "-" + contents.substring(4,contents.length());
                sensorFactoryId.setText(value);


            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_sensor, menu);
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
