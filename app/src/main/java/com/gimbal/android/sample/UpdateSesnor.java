package com.gimbal.android.sample;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ramyaky on 5/1/15.
 */
public class UpdateSesnor extends AsyncTask<String, Void, String> {

    public AsyncResponse sendResult=null;

    public UpdateSesnor(AsyncResponse asyncResponse) {
        sendResult = asyncResponse;
    }

    @Override
    protected String doInBackground(String... params) {

        StringBuffer sb = new StringBuffer();

        try {
            URL url = new URL(params[0]);

            String API_KEY = "Token " + "token=" + "996bdd4685014fcdacd367bfaf4d54f3";
            //String API_KEY = "Token " + "token=" + "f6d9f13961552908d74ef56e783dc5a3";

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("AUTHORIZATION", API_KEY);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);
            if(params[2] == "1"){
                urlConnection.setRequestMethod("POST");
            }else {
                urlConnection.setRequestMethod("PUT");
            }

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
            outputStreamWriter.write(params[1]);
            outputStreamWriter.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while((line = in.readLine()) != null) {
                sb.append(line);
            }
            outputStreamWriter.close();
            in.close();

        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void onPostExecute(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            sendResult.onProcessFinish(jsonObject.toString());

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}