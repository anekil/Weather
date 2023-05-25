package com.pam.weather.detailsfragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pam.weather.R;
import com.pam.weather.WeatherResponse;

public class ForecastDetails1Fragment extends DetailsFragment {
    View rootView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast_details1, container, false);
        return rootView;
    }

    public void loadWeather(WeatherResponse weather){
        if(rootView != null){
            TextView text = rootView.findViewById(R.id.minimal);
            text.setText(String.valueOf(weather.main.temp_min));
            text = rootView.findViewById(R.id.maximal);
            text.setText(String.valueOf(weather.main.temp_max));
            text = rootView.findViewById(R.id.sunrise);
            text.setText(String.valueOf(weather.sys.sunrise));
            text = rootView.findViewById(R.id.sunset);
            text.setText(String.valueOf(weather.sys.sunset));
        }

    }
}