package com.pam.weather.weatherresponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherResponse {
    @SerializedName("city")
    public City city;
    @SerializedName("list")
    public ArrayList<WeatherForDay> list;
}


