package com.example.pjajoo.weatherapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private TextView weatherReport;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                final URL url = new URL(urls[0]);
                final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                final InputStream inputStream = urlConnection.getInputStream();
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                String result = "";
                while (data != -1) {
                    result += (char) data;
                    data = inputStreamReader.read();
                }
                return result;
            } catch (final Exception e) {
                e.printStackTrace();
                Log.e("Exception", "while downloading the contents");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("Contents", result);
            try {
                final JSONObject jsonObject = new JSONObject(result);
                final JSONArray weatherInfoArr = jsonObject.getJSONArray("weather");
                String weatherReportText = "";
                weatherReport.setText("");
                for (int i = 0; i < weatherInfoArr.length(); i++) {
                    final JSONObject weatherReportJson = weatherInfoArr.getJSONObject(i);
                    weatherReportText += weatherReport.getText().toString() +
                            "\n" +
                            weatherReportJson.get("main").toString() +
                            ":" +
                            weatherReportJson.get("description").toString();
                }
                weatherReport.setText(weatherReportText);
                weatherReport.setBackgroundColor(Color.WHITE);
            } catch (final Exception e) {
                e.printStackTrace();
                Log.e("Exception", "while parsing the result string");
            }
        }
    }

    public void getWeather(final View view) {
        String city = ((TextView) findViewById(R.id.city)).getText().toString().toLowerCase();
        String country = ((TextView) findViewById(R.id.country)).getText().toString().toLowerCase();
        try {
            city = URLEncoder.encode(city, "UTF-8");
            country = URLEncoder.encode(city, "UTF-8");
        } catch (final Exception e) {
            e.printStackTrace();
            Log.e("Exception", "while encoding city/country");
        }
        final String apiUrl = "http://www.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&appid=b6907d289e10d714a6e88b30761fae22";
        final DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(apiUrl);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.weatherReport = (TextView) findViewById(R.id.multiLineTextView);
    }
}
