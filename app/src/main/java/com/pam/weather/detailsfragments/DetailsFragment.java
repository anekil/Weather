package com.pam.weather.detailsfragments;

import androidx.fragment.app.Fragment;

import com.pam.weather.weatherresponse.WeatherResponse;

public abstract class DetailsFragment extends Fragment {
    public abstract void loadWeather(WeatherResponse weather);
}
