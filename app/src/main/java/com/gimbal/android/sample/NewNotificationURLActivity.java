package com.gimbal.android.sample;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class NewNotificationURLActivity extends Activity {

    public static String communicationID;
    public String urlValue;
    public EditText urlEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notification_url);

        urlEditText = (EditText) findViewById(R.id.enterUrl);

        Button submitButton = (Button) findViewById(R.id.urlSubmitButton);
        Button cancelButton = (Button) findViewById(R.id.urlCancelButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fecting all available communications and get id from it , as notification url needs that id
                getAllCommunications();


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getAllCommunications() {
        urlValue = urlEditText.getText().toString();
        String BASE_URL = "https://manager.gimbal.com/api/communications";
        FetchSensorsList fetchSensorsList = new FetchSensorsList(new AsyncResponse() {
            @Override
            public void onProcessFinish(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    System.out.println("Printing communication list json : " +jsonArray.get(0));
                    JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
                    communicationID = jsonObject.getString("id");
                    // first delete existing notification
                    if(jsonObject.has("notification")){
                        deleteNotification();
                    }
                        UpdateSesnor updateSesnor = new UpdateSesnor(new AsyncResponse() {
                            @Override
                            public void onProcessFinish(String s) {
                                try {
                                    JSONObject responseJsonObject = new JSONObject(s);
                                    System.out.println("Notification response object : " + responseJsonObject);
                                    Toast.makeText(getApplicationContext(), "Successfully updated URL", Toast.LENGTH_LONG).show();
                                    finish();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        try{
                            JSONObject requestObject = new JSONObject();
                            requestObject.put("title", "MyNotification");
                            requestObject.put("description", "Student material related url");
                            System.out.println("Printing my notification URL : " +urlValue);
                            requestObject.put("url", urlValue);
                            requestObject.put("combine_title_description", false);

                            String BASE_URL = "https://manager.gimbal.com/api/communications/"+communicationID+"/notification";
                            updateSesnor.execute(BASE_URL, requestObject.toString(), "1");

                        }catch (Exception e) {
                            e.printStackTrace();
                        }



                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });
        fetchSensorsList.execute(BASE_URL);

    }

    public void deleteNotification() {
        System.out.println("Inside delete method");
        String BASE_URL = "https://manager.gimbal.com/api/communications/" +communicationID + "/notification";
        System.out.println("Printing my delete url : " + BASE_URL);
        DeleteNotification deleteNotification = new DeleteNotification();
        deleteNotification.execute(BASE_URL);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_notification_url, menu);
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
