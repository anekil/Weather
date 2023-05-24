package com.pam.weather;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastActivity extends AppCompatActivity {
    String keyAPI = "a7801ab3bb1ab1a6e70f97bb4b575006";
    String city;
    String units;
    DetailsAdapter details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        loadingScreen();
        Bundle bundle = getIntent().getExtras();
        city = bundle.getString("cityName");
        boolean imperialUnits = bundle.getBoolean("units");
        if(imperialUnits)
            units = "imperial";
        else
            units = "metric";

        setupDetailsPager();
        Thread callingAPI = new Thread(this::callAPI);
        callingAPI.start();
    }

    private void callAPI() {
        WeatherApiService apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiService.class);
        String encodedCityName;
        try {
            encodedCityName = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            encodedCityName = Normalizer.normalize(city, Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "")
                    .replaceAll("[^\\p{ASCII}]", "");
        }
        Call<WeatherResponse> call = apiService.getCurrentWeatherData(encodedCityName, "4", units, keyAPI);

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
                        textView.setText(Integer.toString(response.code())+ '\n' + call.request().url());
                    }

                } else {
                    errorScreen();
                    TextView textView = findViewById(R.id.errorText);
                    textView.setText(Integer.toString(response.code())+ '\n' + call.request().url());
                    //Toast.makeText(MainActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                errorScreen();
                TextView textView = findViewById(R.id.errorText);
                textView.setText(t.getMessage() + '\n' + call.request().url());
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
        findViewById(R.id.loader).setVisibility(View.GONE);
        findViewById(R.id.errorText).setVisibility(View.GONE);
        findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
    }

    void updateForecast(WeatherResponse weather) {
        TextView text = findViewById(R.id.location);
        text.setText(weather.name);
        text = findViewById(R.id.updated_at);
        text.setText(String.valueOf(weather.dt));
        text = findViewById(R.id.status);
        text.setText(weather.weather.get(0).description);
        text = findViewById(R.id.temp);
        text.setText(String.valueOf(Math.round(weather.main.temp)));
        text = findViewById(R.id.feels_like);
        text.setText(String.valueOf(Math.round(weather.main.feels_like)));
/*
        ForecastDetails1Fragment fragment1 = (ForecastDetails1Fragment) details.createFragment(0);
        text = fragment1.getView().findViewById(R.id.minimal);
        text.setText(String.valueOf(weather.main.temp_min));
        text = fragment1.getView().findViewById(R.id.maximal);
        text.setText(String.valueOf(weather.main.temp_max));
        text = fragment1.getView().findViewById(R.id.sunrise);
        text.setText(String.valueOf(weather.sys.sunrise));
        text = fragment1.getView().findViewById(R.id.sunset);
        text.setText(String.valueOf(weather.sys.sunset));

        ForecastDetails2Fragment fragment2 = (ForecastDetails2Fragment) details.createFragment(1);
        text = fragment2.getView().findViewById(R.id.pressure);
        text.setText(String.valueOf(weather.main.pressure));
        text = fragment2.getView().findViewById(R.id.humidity);
        text.setText(String.valueOf(weather.main.humidity));
        text = fragment2.getView().findViewById(R.id.visibility);
        text.setText(String.valueOf(weather.visibility));
        text = fragment2.getView().findViewById(R.id.wind);
        text.setText(String.valueOf(weather.wind.speed));

        ForecastNextDaysFragment fragment3 = (ForecastNextDaysFragment) details.createFragment(2);*/
    }

/*
    boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        return connected;
    }

    }*/
}
