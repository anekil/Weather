package com.pam.weather;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import com.pam.weather.detailsfragments.DetailsAdapter;
import com.pam.weather.detailsfragments.DetailsFragment;
import com.pam.weather.detailsfragments.ForecastDetails1Fragment;
import com.pam.weather.detailsfragments.ForecastDetails2Fragment;
import com.pam.weather.detailsfragments.ForecastGeneralFragment;
import com.pam.weather.detailsfragments.ForecastNextDaysFragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ForecastActivity extends AppCompatActivity implements ApiCallback {
    boolean isLarge = false;
    ArrayList<DetailsFragment> detailsFragments;
    ViewPager2 detailsPager;
    DetailsAdapter detailsAdapter;
    RefreshTimer refreshTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        FavouritesManager.setCurrentCallback(this);

        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if(screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            isLarge = true;

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
        } else {
            FavouritesManager.refreshCurrent();
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
       if(isLarge){
           detailsFragments = new ArrayList<>();
           FragmentManager fragmentManager = getSupportFragmentManager();
           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
           ForecastGeneralFragment fragment1 = new ForecastGeneralFragment();
           fragmentTransaction.replace(R.id.detailsPager1, fragment1);
           detailsFragments.add(fragment1);
           ForecastNextDaysFragment fragment4 = new ForecastNextDaysFragment();
           fragmentTransaction.replace(R.id.detailsPager2, fragment4);
           detailsFragments.add(fragment4);
           ForecastDetails1Fragment fragment2 = new ForecastDetails1Fragment();
           fragmentTransaction.replace(R.id.detailsPager3, fragment2);
           detailsFragments.add(fragment2);
           ForecastDetails2Fragment fragment3 = new ForecastDetails2Fragment();
           fragmentTransaction.replace(R.id.detailsPager4, fragment3);
           detailsFragments.add(fragment3);

           fragmentTransaction.commit();
        } else {
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
            detailsPager.setAdapter(detailsAdapter);
        }
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
        updateHeading();
        if(isLarge){
            for(DetailsFragment fragment : detailsFragments){
                fragment.loadWeather(FavouritesManager.currentWeather);
            }
        } else {
            detailsAdapter.updateFragments(FavouritesManager.currentWeather);
            detailsAdapter.notifyDataSetChanged();
        }
    }

    void updateHeading(){
        TextView text = findViewById(R.id.location);
        String location = FavouritesManager.currentWeather.city.name + ", " + FavouritesManager.currentWeather.city.country;
        text.setText(location);
        text = findViewById(R.id.updated_at);
        text.setText(FavouritesManager.currentWeather.list.get(0).getFullDt());
        text = findViewById(R.id.lon);
        String lon = Math.round(FavouritesManager.currentWeather.city.coord.lon * 100.0) / 100.0 + " ";
        text.setText(lon);
        text = findViewById(R.id.lat);
        text.setText(String.valueOf(Math.round(FavouritesManager.currentWeather.city.coord.lat * 100.0) / 100.0));
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
    public void onApiResponseAll() {
        showToast("Successfully refreshed");
    }

    @Override
    public void onApiResponseCurrent() {
        updateForecast();
        dataScreen();
        FavouritesManager.refreshAll();
    }

    @Override
    public void onCityNotFound(String city) {
        showToast("Couldn't find city");
        onBackPressed();
    }

    @Override
    public void onApiFailure() {
        showToast("Couldn't connect with API");
    }

    class RefreshTimer {
        private Timer timer;
        private TimerTask timerTask;
        private final long TIMER_DELAY = 100;
        private final long TIMER_INTERVAL = 20000;

        private void startTimer() {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        if(checkInternetConnection()){
                            FavouritesManager.refreshCurrent();
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
