package com.pam.weather;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class DetailsAdapter extends FragmentStateAdapter {
    private WeatherResponse weather;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    public DetailsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        fragmentList.add(new ForecastDetails1Fragment());
        fragmentList.add(new ForecastDetails2Fragment());
        fragmentList.add(new ForecastNextDaysFragment());
    }

    public void updateWeather(WeatherResponse weatherResponse){
        weather = weatherResponse;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch(position) {
            case 1: fragment = ForecastDetails2Fragment.newInstance(weather); break;
            case 2: fragment = ForecastNextDaysFragment.newInstance(weather); break;
            default: fragment = ForecastDetails1Fragment.newInstance(weather); break;
        };
        return fragment;
    }
    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}