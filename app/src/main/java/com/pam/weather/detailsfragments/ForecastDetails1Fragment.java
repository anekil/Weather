package com.pam.weather.detailsfragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pam.weather.FavouritesData;
import com.pam.weather.R;
import com.pam.weather.weatherresponse.WeatherResponse;

public class ForecastDetails1Fragment extends DetailsFragment {
    View rootView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forecast_details1, container, false);
        return rootView;
    }

    public void loadWeather(WeatherResponse weather){
        if(rootView == null)
            return;

        TextView text = rootView.findViewById(R.id.minimal);
        text.setText(weather.list.get(0).main.temp_min + FavouritesData.getUnits().label);
        text = rootView.findViewById(R.id.maximal);
        text.setText(weather.list.get(0).main.temp_max + FavouritesData.getUnits().label);
        text = rootView.findViewById(R.id.sunrise);
        text.setText(String.valueOf(weather.city.sunrise));
        text = rootView.findViewById(R.id.sunset);
        text.setText(String.valueOf(weather.city.sunset));
    }
}