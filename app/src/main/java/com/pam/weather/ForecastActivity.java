package com.pam.weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.pam.weather.detailsfragments.DetailsAdapter;
import com.pam.weather.detailsfragments.DetailsFragment;
import com.pam.weather.weatherresponse.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastActivity extends AppCompatActivity {
    private static final String API_KEY = "a7801ab3bb1ab1a6e70f97bb4b575006";
    String city;
    Units units;
    WeatherResponse currentWeather;
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
            units = Units.IMPERIAL;
        else
            units = Units.METRIC;

        setupDetailsPager();
        if(checkInternetConnection()){
            Thread callingAPI = new Thread(this::callAPI);
            callingAPI.start();
        } else {
            Toast.makeText(ForecastActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            currentWeather = FavouritesData.getWeather(city);
            if(currentWeather != null) {
                updateForecast();
                dataScreen();
            }
        }

    }

    private void callAPI() {
        WeatherApiService apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getCurrentWeatherData(city, "4", units.name(), API_KEY);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    currentWeather = response.body();
                    if (currentWeather != null) {
                        updateForecast();
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
        detailsPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                DetailsFragment fragment = (DetailsFragment) ForecastActivity.this.getSupportFragmentManager().findFragmentById(detailsPager.getCurrentItem());
                if(fragment != null){
                    fragment.loadWeather(currentWeather);
                }
            }
        });
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

    void updateForecast() {


        detailsAdapter.setWeather(currentWeather);
    }



    boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }

}
