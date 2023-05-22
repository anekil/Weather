package com.pam.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ForecastActivity extends AppCompatActivity {
    String keyAPI = "a7801ab3bb1ab1a6e70f97bb4b575006";
    String city = "Łódź,PL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
/*
        Button forecastBtn = findViewById(R.id.forecastBtn);
        Button generalInfoBtn = findViewById(R.id.generalInfoBtn);

        forecastBtn.setOnClickListener((view) ->{
           showInfo(ForecastFragment.class);
        });

        generalInfoBtn.setOnClickListener((view) ->{
            showInfo(GeneralInfoFragment.class);
        });*/
        /*
        WeatherTask task = new WeatherTask();
        task.execute();*/
    }

    void showInfo(Class<? extends Fragment> fragmentClass){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragmentClass, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }

    void loading(){
        findViewById(R.id.loader).setVisibility(View.VISIBLE);
        findViewById(R.id.mainContainer).setVisibility(View.GONE);
        findViewById(R.id.errorText).setVisibility(View.GONE);
    }

    void showError(){
        findViewById(R.id.loader).setVisibility(View.GONE);
        findViewById(R.id.errorText).setVisibility(View.VISIBLE);
    }

    void updateCoord(JSONObject object) throws JSONException {
        double lon = object.getDouble("lon");
        double lat = object.getDouble("lat");
    }

    void updateWeather(JSONObject object) throws JSONException {
        String weatherDescription = object.getString("description");

        TextView text = findViewById(R.id.status);
        text.setText(weatherDescription.toUpperCase());
    }

    void updateMain(JSONObject object) throws JSONException {
        String temp = object.getString("temp") + "℃";
        String feelsLike = object.getString("feels_like") + "℃";
        String tempMin = "Min Temp: " + object.getString("temp_min")+"°C";
        String tempMax = "Max Temp: " + object.getString("temp_max")+"°C";
        String pressure = object.getString("pressure");
        String humidity = object.getString("humidity");

        TextView text = findViewById(R.id.temp);
        text.setText(temp);
        text = findViewById(R.id.temp_min);
        text.setText(tempMin);
        text = findViewById(R.id.temp_max);
        text.setText(tempMax);

        text = findViewById(R.id.pressure);
        text.setText(pressure);
        text = findViewById(R.id.humidity);
        text.setText(humidity);
    }
    void updateWind(JSONObject object) throws JSONException {
        String windSpeed = object.getString("speed");

        TextView text = findViewById(R.id.wind);
        text.setText(windSpeed);
    }
    void updateSys(JSONObject object) throws JSONException {
        long sunrise = object.getLong("sunrise");
        long sunset = object.getLong("sunset");
        //String address = object.getString("name")+", "+object.getString("country");

        TextView text = findViewById(R.id.sunrise);
        text.setText(new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.ENGLISH).format(new Date(sunrise*1000)));
        text = findViewById(R.id.sunset);
        text.setText(new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.ENGLISH).format(new Date(sunset*1000)));
        text = findViewById(R.id.address);
        text.setText(city);
    }


    void updateForecast(JSONObject object)  {
        try {
            updateCoord(object.getJSONObject("coord"));
            updateWeather(object.getJSONArray("weather").getJSONObject(0));
            updateMain(object.getJSONObject("main"));
            updateWind(object.getJSONObject("wind"));
            updateSys(object.getJSONObject("sys"));

            int visibility = object.getInt("visibility");

            long updateAt = object.getLong("dt");
            String updateAtText = "Updated at: " + new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.ENGLISH).format(new Date(updateAt*1000));
            TextView text = findViewById(R.id.updated_at);
            text.setText(updateAtText);
        } catch (JSONException e) {
            showError();
        }
    }

    boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        return connected;
    }

    class WeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loading();
        }


        @Override
        protected String doInBackground(String... strings) {
            String response;
            try {
                // TODO add units
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + keyAPI);
                URLConnection urlConn = url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                response = stringBuilder.toString();
            } catch (IOException e) {
                response = null;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject object = new JSONObject(result);
                updateForecast(object);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}