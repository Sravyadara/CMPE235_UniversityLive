package com.gimbal.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class LocateSensors extends Activity {

    private GoogleMap map;
    private BitmapDescriptor bitmapDescriptor;
    private GroundOverlayOptions groundOverlayOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_sensors);
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> floors = new ArrayList<String>();
        floors.add("Floor 1");
        floors.add("Floor 2");
        floors.add("Floor 3");
        floors.add("Floor 4");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, floors);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        try {


            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.sensorsMap)).getMap();
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.lucasbuilding1);
            drawMarkers("Floor 1");

            LatLng southWest = new LatLng(37.406888, -121.979740);
            LatLng northEast = new LatLng(37.407376, -121.979104);
            LatLngBounds latLngBounds = new LatLngBounds(southWest, northEast);

            groundOverlayOptions = new GroundOverlayOptions();
            groundOverlayOptions.positionFromBounds(latLngBounds);
            groundOverlayOptions.image(bitmapDescriptor);
            groundOverlayOptions.transparency(0.5f);
            map.addGroundOverlay(groundOverlayOptions);



        }catch (Exception e) {
            e.printStackTrace();
        }
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //LatLngBounds bounds = builder.build();
                //map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 17));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.407376, -121.979104),18);
                map.animateCamera(cameraUpdate);

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
                if(parent.getItemAtPosition(position).toString() == "Floor 2") {
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.firstdesign);

                }else if(parent.getItemAtPosition(position).toString() == "Floor 3") {
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.lucasbuilding3);
                }else if(parent.getItemAtPosition(position).toString() == "Floor 4") {
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.lucasbuilding4);
                }else {
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.lucasbuilding1);
                }
                groundOverlayOptions.image(bitmapDescriptor);
                map.clear();
                drawMarkers(parent.getItemAtPosition(position).toString());
                map.addGroundOverlay(groundOverlayOptions);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void drawMarkers(String floor) {
        String requiredString = "_F" + floor.split(" ")[1];

        try {
            JSONArray jsonArray = new JSONArray(getIntent().getExtras().getString("sensorObject"));
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("name").contains(requiredString)) {
                    System.out.println("Name of the sensor : " + jsonObject.getString("name"));
                    Double latitude = jsonObject.getDouble("latitude");
                    Double longitude = jsonObject.getDouble("longitude");
                    LatLng latLng = new LatLng(latitude, longitude);
                    map.addMarker(new MarkerOptions().position(latLng));
                    //builder.include(latLng);
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_locate_sensors, menu);
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
