package com.pam.weather;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.pam.weather.detailsfragments.DetailsAdapter;
import com.pam.weather.detailsfragments.DetailsFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastActivity extends AppCompatActivity {
    String keyAPI = "a7801ab3bb1ab1a6e70f97bb4b575006";
    String city;
    String units;
    ViewPager2 detailsPager;
    DetailsAdapter detailsAdapter;

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
        detailsPager = findViewById(R.id.detailsPager);
        detailsAdapter = new DetailsAdapter(getSupportFragmentManager(), getLifecycle());
        detailsPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        detailsPager.setAdapter(detailsAdapter);
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

        detailsAdapter.setWeather(weather);

        for(int i=0; i<detailsAdapter.getItemCount(); i++){
            DetailsFragment fragment = (DetailsFragment) detailsAdapter.createFragment(i);
            fragment.loadWeather(weather);
        }
    }

/*
    boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        return connected;
    }*/

    }
}
