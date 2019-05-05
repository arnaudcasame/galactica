package com.example.android.galactica;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Arnaud Casamé on 7/8/2018.
 */

public class HttpHandler {

    HttpHandler(){}

    public static List<News> makeServiceCall(String reqUrl){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            Log.i("http-code", ""+conn.getResponseCode());
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return extractFeatureFromJson(response);
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    private static List<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {
            JSONObject jsonObj = new JSONObject(newsJSON);

            JSONObject result = jsonObj.getJSONObject("response");

            JSONArray results = result.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject c = results.getJSONObject(i);
                String title = c.getString("webTitle");
                String description = c.getString("sectionName");
                String link = c.getString("webUrl");
                String date = c.getString("webPublicationDate");
                String author = "";

                JSONArray contributorArray = c.getJSONArray("tags");

                for(int j = 0; j < contributorArray.length(); j++){
                    JSONObject d = contributorArray.getJSONObject(j);
                     author += d.getString("webTitle");
                     if(contributorArray.length() > 0 && j != (contributorArray.length() - 1)){
                         author += ", ";
                     }
                }

                newsList.add(new News(title, description, link, date, author));
            }

        } catch (JSONException e) {
            Log.e("HttpHandler", "Problem parsing the news JSON results", e);
        }

        return newsList;
    }
}
