package com.pam.weather.detailsfragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pam.weather.FavouritesManager;
import com.pam.weather.R;
import com.pam.weather.weatherresponse.WeatherResponse;

public class ForecastDetails2Fragment extends DetailsFragment {
    View rootView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forecast_details2, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loadWeather(FavouritesManager.currentWeather);
    }

    public void loadWeather(WeatherResponse weather){
        if(rootView == null)
            return;

        TextView text = rootView.findViewById(R.id.pressure);
        text.setText(Math.round(weather.list.get(0).main.pressure) + "hPa");
        text = rootView.findViewById(R.id.humidity);
        text.setText(Math.round(weather.list.get(0).main.humidity) + "%");
        text = rootView.findViewById(R.id.visibility);
        text.setText(Math.round(weather.list.get(0).visibility) + "m");
        text = rootView.findViewById(R.id.wind);
        text.setText(weather.list.get(0).wind.speed + weather.units.speed_label);
    }
}