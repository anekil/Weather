package com.pam.weather;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastActivity extends AppCompatActivity {
    String keyAPI = "a7801ab3bb1ab1a6e70f97bb4b575006";
    String city = "Łódź,PL";
    String units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        loadingScreen();
        Bundle bundle = getIntent().getExtras();
        boolean metricUnits = bundle.getBoolean("type");
        if(metricUnits)
            units = "metric";
        else
            units = "imperial";

        setupDetailsPager();
        Thread callingAPI = new Thread(this::callAPI);
        callingAPI.start();
    }

    private void callAPI() {
        WeatherApiService apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getCurrentWeatherData(city, "4", units, keyAPI);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
                        updateForecast(weatherResponse);
                        dataScreen();
                    } else {
                        errorScreen();
                        TextView textView = findViewById(R.id.errorText);
                        textView.setText(Integer.toString(response.code()));
                    }

                } else {
                    errorScreen();
                    TextView textView = findViewById(R.id.errorText);
                    textView.setText(Integer.toString(response.code()));
                    //Toast.makeText(MainActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                errorScreen();
                TextView textView = findViewById(R.id.errorText);
                textView.setText(t.getMessage());
            }
        });
    }

    private void setupDetailsPager() {
        ViewPager2 detailsPager = findViewById(R.id.detailsPager);
        DetailsAdapter adapter = new DetailsAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(new ForecastDetails1Fragment());
        adapter.addFragment(new ForecastDetails2Fragment());
        adapter.addFragment(new ForecastNextDaysFragment());
        detailsPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        detailsPager.setAdapter(adapter);
    }

    void loadingScreen(){
        findViewById(R.id.loader).setVisibility(View.VISIBLE);
        findViewById(R.id.mainContainer).setVisibility(View.GONE);
        findViewById(R.id.errorText).setVisibility(View.GONE);
    }

    void errorScreen(){
        findViewById(R.id.loader).setVisibility(View.GONE);
        findViewById(R.id.errorText).setVisibility(View.VISIBLE);
    }

    void dataScreen(){
        findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
    }

    void updateForecast(WeatherResponse weather) {
        TextView text = findViewById(R.id.location);
        text.setText(weather.name);
        text = findViewById(R.id.updated_at);
        text.setText((int) weather.dt);
        text = findViewById(R.id.status);
        text.setText(weather.weather.get(0).description);
        text = findViewById(R.id.temp);
        text.setText((int) weather.main.temp);
        text = findViewById(R.id.feels_like);
        text.setText((int) weather.main.feels_like);
        text = findViewById(R.id.minimal);
        text.setText((int) weather.main.temp_min);
        text = findViewById(R.id.maximal);
        text.setText((int) weather.main.temp_max);
        text = findViewById(R.id.temp);
        text.setText((int) weather.main.temp);
        text = findViewById(R.id.pressure);
        text.setText((int) weather.main.pressure);
        text = findViewById(R.id.humidity);
        text.setText((int) weather.main.humidity);
        text = findViewById(R.id.visibility);
        text.setText((int) weather.visibility);
        text = findViewById(R.id.wind);
        text.setText((int) weather.wind.speed);
        text = findViewById(R.id.sunrise);
        text.setText((int) weather.sys.sunrise);
        text = findViewById(R.id.sunset);
        text.setText((int) weather.sys.sunset);
    }


        /*TextView text = findViewById(R.id.status);
        text.setText(weather.weather.toUpperCase());
        text = findViewById(R.id.temp);
        text.setText(temp);*/
            /*
            text = findViewById(R.id.minimal);
            text.setText(tempMin);
            text = findViewById(R.id.maximal);
            text.setText(tempMax);

            text = findViewById(R.id.pressure);
            text.setText(pressure);
            text = findViewById(R.id.humidity);
            text.setText(humidity);
            text = findViewById(R.id.sunrise);
            text.setText(new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.ENGLISH).format(new Date(sunrise*1000)));
            text = findViewById(R.id.sunset);
            text.setText(new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.ENGLISH).format(new Date(sunset*1000)));
            text = findViewById(R.id.location);
            text.setText(city);
            text = findViewById(R.id.wind);
            text.setText(windSpeed);

            String updateAtText = "Updated at: " + new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.ENGLISH).format(new Date(dt*1000));
            text = findViewById(R.id.updated_at);
            text.setText(updateAtText);
    }

/*
    void updateForecast(JSONObject object)  {
        try {
            JSONObject coord = object.getJSONObject("coord");
            JSONObject weather = object.getJSONArray("weather").getJSONObject(0);
            JSONObject main = object.getJSONObject("main");
            JSONObject wind = object.getJSONObject("wind");
            JSONObject sys = object.getJSONObject("sys");
            int visibility = object.getInt("visibility");
            long dt = object.getLong("dt");

            String weatherDescription = weather.getString("description");
            double lon = coord.getDouble("lon");
            double lat = coord.getDouble("lat");
            String temp = main.getString("temp") + "℃";
            String feelsLike = main.getString("feels_like") + "℃";
            String tempMin = "Min Temp: " + main.getString("temp_min")+"°C";
            String tempMax = "Max Temp: " + main.getString("temp_max")+"°C";
            String pressure = main.getString("pressure");
            String humidity = main.getString("humidity");
            long sunrise = sys.getLong("sunrise");
            long sunset = sys.getLong("sunset");
            String windSpeed = wind.getString("speed");
            //String address = object.getString("name")+", "+object.getString("country");

            TextView text = findViewById(R.id.status);
            text.setText(weatherDescription.toUpperCase());
            text = findViewById(R.id.temp);
            text.setText(temp);
            /*
            text = findViewById(R.id.minimal);
            text.setText(tempMin);
            text = findViewById(R.id.maximal);
            text.setText(tempMax);

            text = findViewById(R.id.pressure);
            text.setText(pressure);
            text = findViewById(R.id.humidity);
            text.setText(humidity);
            text = findViewById(R.id.sunrise);
            text.setText(new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.ENGLISH).format(new Date(sunrise*1000)));
            text = findViewById(R.id.sunset);
            text.setText(new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.ENGLISH).format(new Date(sunset*1000)));
            text = findViewById(R.id.location);
            text.setText(city);
            text = findViewById(R.id.wind);
            text.setText(windSpeed);

            String updateAtText = "Updated at: " + new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.ENGLISH).format(new Date(dt*1000));
            text = findViewById(R.id.updated_at);
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

    }*/
}
