package com.swati.s_labs;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by swati on 16/12/15.
 */
public class SendingRequest {
    String GOOGLE_KEY="AIzaSyAFUvi8K_4EPO40Ymx1GP95A0zQh6jBMnE";
    final String latitude = "17.3700";
    final String longtitude = "78.4800";


    public String send(){
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longtitude + "&radius=20000&sensor=true&key=" + GOOGLE_KEY);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }

    }

}
