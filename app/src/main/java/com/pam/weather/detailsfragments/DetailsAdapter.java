package com.pam.weather.detailsfragments;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.pam.weather.weatherresponse.WeatherResponse;

import java.util.ArrayList;

public class DetailsAdapter extends FragmentStateAdapter {
    private final ArrayList<DetailsFragment> fragmentList;

    public DetailsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        fragmentList = new ArrayList<>();
        fragmentList.add(new ForecastGeneralFragment());
        fragmentList.add(new ForecastDetails1Fragment());
        fragmentList.add(new ForecastDetails2Fragment());
        fragmentList.add(new ForecastNextDaysFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }
    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
    @Override
    public long getItemId(int position) {
        return fragmentList.get(position).hashCode();
    }

    public void updateFragments(WeatherResponse weather) {
        for (DetailsFragment fragment : fragmentList) {
            fragment.loadWeather(weather);
        }
    }
}