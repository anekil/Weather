package com.pam.weather;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ForecastDetails1Fragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            //ustawianie
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast_details1, container, false);
    }

    public static ForecastDetails1Fragment newInstance(WeatherResponse weather) {
        ForecastDetails1Fragment fragment = new ForecastDetails1Fragment();
        Bundle args = new Bundle();
        args.putParcelable("weather", weather);
        fragment.setArguments(args);
        return fragment;
    }
}