package com.pam.weather.detailsfragments;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pam.weather.WeatherResponse;

import java.util.ArrayList;

public class DetailsAdapter extends FragmentStateAdapter {
    private ArrayList<DetailsFragment> fragmentList = new ArrayList<>();
    WeatherResponse weather = null;

    public DetailsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        fragmentList.add(new ForecastDetails1Fragment());
        fragmentList.add(new ForecastDetails2Fragment());
        fragmentList.add(new ForecastNextDaysFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        DetailsFragment fragment = fragmentList.get(position);
        fragment.loadWeather(weather);
        return fragment;
    }
    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public void setWeather(WeatherResponse weatherResponse){
        weather = weatherResponse;
    }
}