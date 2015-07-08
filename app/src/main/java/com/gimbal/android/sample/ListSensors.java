package com.gimbal.android.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


public class ListSensors extends Activity {

    private SensorsListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sensors);

        adapter = new SensorsListAdapter();
        ListView sensorsListView = (ListView) findViewById(R.id.sensorsListView);
        sensorsListView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_sensors, menu);
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

    public class SensorsListAdapter extends BaseAdapter {

        String jsonArrayString = getIntent().getExtras().getString("sensorObject");
        JSONArray jsonArray = getSensorObject(jsonArrayString);



        @Override
        public int getCount() {
            return jsonArray.length();
        }

        @Override
        public Object getItem(int position) {
            try {
                return jsonArray.get(position);
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try {
                if(convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) ListSensors.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.sensor_record, parent, false);
                }
                TextView sensorNameTextView = (TextView) convertView.findViewById(R.id.sensorName);
                TextView sensorUuidTextView = (TextView) convertView.findViewById(R.id.sensorUuid);
                TextView sensorVendorTextView = (TextView) convertView.findViewById(R.id.sensorVendor);

                JSONObject sensorRecord = jsonArray.getJSONObject(position);

                sensorNameTextView.setText("Name : " + sensorRecord.getString("name"));
                sensorVendorTextView.setText("Factory ID : " +sensorRecord.getString("factory_id"));
                sensorUuidTextView.setText("ID : " +sensorRecord.getString("id"));

            }catch (Exception e){
                e.printStackTrace();
            }


            return convertView;
        }

        public JSONArray getSensorObject(String s) {
            JSONArray myArray = new JSONArray();
            try{
                myArray = new JSONArray(s);

            }catch (Exception e){
                e.printStackTrace();
            }
            return myArray;
        }
    }
}
