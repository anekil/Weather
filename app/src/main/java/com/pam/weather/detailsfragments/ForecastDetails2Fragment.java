package com.pam.weather.detailsfragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pam.weather.R;
import com.pam.weather.WeatherResponse;

public class ForecastDetails2Fragment extends DetailsFragment {
    View rootView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast_details2, container, false);
    }

    public void loadWeather(WeatherResponse weather){
        if(rootView != null){
            TextView text = rootView.findViewById(R.id.pressure);
            text.setText(String.valueOf(weather.main.pressure));
            text = rootView.findViewById(R.id.humidity);
            text.setText(String.valueOf(weather.main.humidity));
            text = rootView.findViewById(R.id.visibility);
            text.setText(String.valueOf(weather.visibility));
            text = rootView.findViewById(R.id.wind);
            text.setText(String.valueOf(weather.wind.speed));
        }
    }
}