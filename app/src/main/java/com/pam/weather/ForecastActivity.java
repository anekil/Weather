package com.pam.weather;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import com.pam.weather.detailsfragments.DetailsAdapter;
import com.pam.weather.detailsfragments.DetailsFragment;
import java.util.Timer;
import java.util.TimerTask;

public class ForecastActivity extends AppCompatActivity implements ApiCallback {
    ViewPager2 detailsPager;
    DetailsAdapter detailsAdapter;
    RefreshTimer refreshTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        FavouritesManager.setCurrentCallback(this);

        findViewById(R.id.refreshAllBtn).setOnClickListener((view) -> {
            updateForecast();
            FavouritesManager.refreshCurrent();
            FavouritesManager.refreshAll();
        });

        loadingScreen();
        setupDetailsPager();
        if(!checkInternetConnection()){
            updateForecast();
            dataScreen();
        }
        refreshTimer = new RefreshTimer();
        refreshTimer.startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        refreshTimer.stopTimer();
    }

    private void setupDetailsPager() {
        detailsPager = findViewById(R.id.detailsPager);
        detailsPager.setOffscreenPageLimit(3);
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

        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if(screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE){
            System.out.println("Tablet");
        }

        detailsPager.setAdapter(detailsAdapter);
    }


    void loadingScreen(){
        findViewById(R.id.loader).setVisibility(View.VISIBLE);
        findViewById(R.id.mainContainer).setVisibility(View.GONE);
    }

    void dataScreen(){
        findViewById(R.id.loader).setVisibility(View.GONE);
        findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
    }

    void updateForecast() {
        TextView text = findViewById(R.id.location);
        String location = FavouritesManager.currentWeather.city.name + ", " + FavouritesManager.currentWeather.city.country;
        text.setText(location);
        text = findViewById(R.id.updated_at);
        text.setText(FavouritesManager.currentWeather.list.get(0).getFullDt());
        text = findViewById(R.id.lon);
        text.setText(Math.round(FavouritesManager.currentWeather.city.coord.lon * 100.0) / 100.0 + " ");
        text = findViewById(R.id.lat);
        text.setText(String.valueOf(Math.round(FavouritesManager.currentWeather.city.coord.lat * 100.0) / 100.0));

        detailsAdapter.updateFragments(FavouritesManager.currentWeather);
        detailsAdapter.notifyDataSetChanged();
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
        private final long TIMER_DELAY = 20;
        private final long TIMER_INTERVAL = 20000;

        private void startTimer() {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        if(checkInternetConnection()){
                            FavouritesManager.refreshCurrent();
                            FavouritesManager.refreshAll();
                        } else {
                            showToast("No internet connection");
                        }
                    });
                }
            };
            timer.schedule(timerTask, TIMER_DELAY, TIMER_INTERVAL);
        }

        private void stopTimer() {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (timerTask != null) {
                timerTask.cancel();
                timerTask = null;
            }
        }
    }
}
