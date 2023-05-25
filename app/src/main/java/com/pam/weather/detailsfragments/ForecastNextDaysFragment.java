package com.pam.weather.detailsfragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pam.weather.R;
import com.pam.weather.WeatherResponse;

public class ForecastNextDaysFragment extends DetailsFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast_next_days, container, false);
    }

    @Override
    public void loadWeather(WeatherResponse weather) {
        // TODO
    }
}