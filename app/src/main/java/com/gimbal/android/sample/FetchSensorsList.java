package com.gimbal.android.sample;

import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ramyaky on 5/1/15.
 */
public class FetchSensorsList extends AsyncTask<String, Void, String> {

    public AsyncResponse sendResult=null;

    public FetchSensorsList(AsyncResponse asyncResponse) {
        sendResult = asyncResponse;
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(params[0]);
            URLConnection urlConnection = url.openConnection();
            //String API_KEY = "Token " + "token=" + "f6d9f13961552908d74ef56e783dc5a3";
           // String API_KEY = "Token " + "token=" + "996bdd4685014fcdacd367bfaf4d54f3";
            String API_KEY = "Token " + "token=" + "c6d5055280a052cb4376685f691ad477";

            urlConnection.setRequestProperty("AUTHORIZATION", API_KEY);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            while((line = in.readLine()) != null) {
                sb.append(line);
            }
            JSONArray jsonArray = new JSONArray(sb.toString());

        } catch (MalformedURLException e) {
            System.out.println("Malformed Exception");
            e.printStackTrace();
        }catch (IOException e) {
            System.out.println("IO Exception");
            e.printStackTrace();
        }catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void onPostExecute(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            sendResult.onProcessFinish(jsonArray.toString());

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
