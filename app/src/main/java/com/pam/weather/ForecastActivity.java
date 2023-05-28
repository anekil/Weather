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
import com.pam.weather.weatherresponse.WeatherResponse;
import java.util.Timer;
import java.util.TimerTask;

public class ForecastActivity extends AppCompatActivity implements ApiCallback {
    ViewPager2 detailsPager;
    DetailsAdapter detailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        FavouritesManager.setCurrentCallback(this);

        loadingScreen();
        setupDetailsPager();
        if(!checkInternetConnection()){
            updateForecast();
            dataScreen();
        }
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
                    fragment.loadWeather(FavouritesManager.currentWeather);
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
        text.setText(FavouritesManager.currentWeather.city.name);
        text = findViewById(R.id.updated_at);
        text.setText(FavouritesManager.currentWeather.list.get(0).getFullDt());

        for (int i = 0; i < detailsAdapter.getItemCount(); i++) {
            DetailsFragment fragment = (DetailsFragment) detailsAdapter.createFragment(i);
            fragment.loadWeather(FavouritesManager.currentWeather);
        }
    }

    boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }

    void showToast(String message){
        Toast.makeText(ForecastActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onApiResponseAll(boolean received) {
        if(received){
            showToast("Successfully refreshed");
        } else {
            showToast("Couldn't connect with API");
        }
    }

    @Override
    public void onApiResponseCurrent(boolean received) {
        if(!received){
            showToast("Couldn't connect with API");
            return;
        }
        updateForecast();
        dataScreen();
    }

    @Override
    public void onApiFailure(Throwable t) {
        showToast("Couldn't connect with API");
    }

    class RefreshTimer {
        private Timer timer;
        private TimerTask timerTask;
        private final long TIMER_DELAY = 20000;
        private final long TIMER_INTERVAL = 20000;

        private void startTimer() {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        Toast.makeText(ForecastActivity.this, "Timer", Toast.LENGTH_SHORT).show();
                        if(checkInternetConnection()){
                            FavouritesManager.refreshCurrent();
                            FavouritesManager.refreshAll();
                            Toast.makeText(ForecastActivity.this, "Automated refresh succeeded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForecastActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
            timer.schedule(timerTask, TIMER_DELAY, TIMER_INTERVAL);
        }

    }
}
