package com.gimbal.android.sample;

import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ramyaky on 5/7/15.
 */
public class DeleteNotification extends AsyncTask<String,Void,Integer> {
    @Override
    protected Integer doInBackground(String... params) {
        int responseCode =0;
        try {
            URL url = new URL(params[0]);
            System.out.println("Inside Async Task");
            String API_KEY = "Token " + "token=" + "996bdd4685014fcdacd367bfaf4d54f3";
            //String API_KEY = "Token " + "token=" + "f6d9f13961552908d74ef56e783dc5a3";

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("AUTHORIZATION", API_KEY);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("DELETE");

            urlConnection.connect();
            responseCode = urlConnection.getResponseCode();
            System.out.println("Pritning return code : " +responseCode);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return responseCode;
    }

    @Override
    public void onPostExecute(Integer result) {
        //String myObjectString = result.split("_")[0];
        //String statusCode = result.split("_")[1];
        if(result == 200) {
            System.out.println("Successfully deleted notification");
        }

    }
}
