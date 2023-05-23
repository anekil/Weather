package com.pam.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ForecastDetails2Fragment extends Fragment {
    /*
     public ForecastDetails1Fragment() {
     }

     public static ForecastDetails1Fragment newInstance(String param1, String param2) {
         ForecastDetails1Fragment fragment = new ForecastDetails1Fragment();
         return fragment;
     }

     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
     }
 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast_details2, container, false);
    }
}