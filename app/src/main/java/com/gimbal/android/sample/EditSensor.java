package com.gimbal.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONObject;


public class EditSensor extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sensor);

        try {
            //System.out.println("Corresponding sensor details : " + getIntent().getExtras().getString("sensorObject"));
            final JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("sensorObject"));
            final String factoryId = jsonObject.getString("factory_id");

            final EditText sensorNameEditText = (EditText) findViewById(R.id.sensorNameValue);
            TextView uuidTextView = (TextView) findViewById(R.id.sensorUuidValue);
            TextView factoryIdTextView = (TextView) findViewById(R.id.sensorFactoryIdValue);
            final EditText latitudeEditText = (EditText) findViewById(R.id.sensorLatitudeValue);
            final EditText longitudeEditText = (EditText) findViewById(R.id.sensorLongitudeValue);
            final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
            Button submitButton = (Button)findViewById(R.id.submitButton);
            Button cancelButton = (Button) findViewById(R.id.cancelButton);

            sensorNameEditText.setText(jsonObject.getString("name"));
            uuidTextView.setText(jsonObject.getString("id"));
            factoryIdTextView.setText(jsonObject.getString("factory_id"));
            latitudeEditText.setText(jsonObject.getString("latitude"));
            longitudeEditText.setText(jsonObject.getString("longitude"));

            final String BASE_URL = "https://manager.gimbal.com/api/beacons/" + jsonObject.getString("factory_id");

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(sensorNameEditText.getText().toString().length() == 0) {
                        sensorNameEditText.setError(Html.fromHtml("<font color='green'>Name can not be null</font>"));
                    }else if(latitudeEditText.getText().toString().length() == 0) {
                        latitudeEditText.setError(Html.fromHtml("<font color='green'>Latitude can not be null</font>"));
                    }else if(longitudeEditText.getText().toString().length() == 0) {
                        longitudeEditText.setError(Html.fromHtml("<font color='green'>Longitude can not be null</font>"));
                    }else {

                        UpdateSesnor updateSesnor = new UpdateSesnor(new AsyncResponse() {
                            @Override
                            public void onProcessFinish(String s) {
                                try {
                                    JSONObject responseJsonObject = new JSONObject(s);
                                    System.out.println("Printing PUT request Response in EditSensor Activity : " + responseJsonObject);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            JSONObject requestObject = new JSONObject();
                            requestObject.put("name", sensorNameEditText.getText().toString());
                            requestObject.put("latitude", Double.parseDouble(latitudeEditText.getText().toString()));
                            requestObject.put("longitude", Double.parseDouble(longitudeEditText.getText().toString()));
                            int checkedMode = radioGroup.getCheckedRadioButtonId();
                            final RadioButton visibilityMode = (RadioButton) findViewById(checkedMode);
                            requestObject.put("visibility", visibilityMode.getText().toString());

                            updateSesnor.execute(BASE_URL, requestObject.toString(), "0");
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

        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_sensor, menu);
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
