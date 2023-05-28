package com.pam.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.pam.weather.detailsfragments.DetailsAdapter;
import com.pam.weather.detailsfragments.DetailsFragment;
import com.pam.weather.weatherresponse.WeatherForDay;
import com.pam.weather.weatherresponse.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastActivity extends AppCompatActivity {
    private static final String API_KEY = "a7801ab3bb1ab1a6e70f97bb4b575006";
    SharedPreferences sharedPreferences;
    String city;
    WeatherResponse currentWeather;
    ViewPager2 detailsPager;
    DetailsAdapter detailsAdapter;
    Units units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        loadingScreen();
        Bundle bundle = getIntent().getExtras();
        city = bundle.getString("cityName");
        units = (Units) bundle.getSerializable("units");

        sharedPreferences = getSharedPreferences("weather", Context.MODE_PRIVATE);

        setupDetailsPager();
        new RefreshTimer().startTimer();
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
        TextView text = findViewById(R.id.location);
        text.setText(currentWeather.city.name);
        text = findViewById(R.id.updated_at);
        text.setText(currentWeather.list.get(0).getDt());

        for (int i = 0; i < detailsAdapter.getItemCount(); i++) {
            DetailsFragment fragment = (DetailsFragment) detailsAdapter.createFragment(i);
            fragment.loadWeather(currentWeather);
        }
    }


    private void callAPI() {
        WeatherApiService apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getCurrentWeatherData(city, "25", units.name(), API_KEY);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    currentWeather = response.body();
                    if (currentWeather != null) {
                        currentWeather.units = units;
                        ArrayList<WeatherForDay> days = new ArrayList<>();
                        for(int i=0; i<=24; i+=8){
                            days.add(currentWeather.list.get(i));
                        }
                        currentWeather.list = days;
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

    boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }
/*
    private void updateFragments() {
        ViewPager2 viewPager = findViewById(R.id.viewPager); // Replace with your ViewPager2 ID
        YourPagerAdapter adapter = (YourPagerAdapter) viewPager.getAdapter(); // Replace with your adapter class

        for (int i = 0; i < adapter.getItemCount(); i++) {
            Fragment fragment = adapter.createFragment(i);
            if (fragment instanceof YourFragment) {
                YourFragment yourFragment = (YourFragment) fragment;
                yourFragment.updateFragment();
            }
        }
    }
*/


    class RefreshTimer {
        private Timer timer;
        private TimerTask timerTask;
        private final long TIMER_DELAY = 0;
        private final long TIMER_INTERVAL = 20000;

        private void startTimer() {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        Toast.makeText(ForecastActivity.this, "Timer", Toast.LENGTH_SHORT).show();

                        if(checkInternetConnection()){
                            Thread callingAPI = new Thread(ForecastActivity.this::callAPI);
                            callingAPI.start();
                        } else {
                            Toast.makeText(ForecastActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            currentWeather = FavouritesData.getWeather(city);
                            if(currentWeather != null) {
                                updateForecast();
                                dataScreen();
                            }
                        }
                    });
                }
            };
            timer.schedule(timerTask, TIMER_DELAY, TIMER_INTERVAL);
        }

    }
}
