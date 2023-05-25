package com.pam.weather.weatherresponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherForDay {
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("visibility")
    public float visibility;
    @SerializedName("weather")
    public ArrayList<Weather> weather = new ArrayList<>();
    @SerializedName("dt_text")
    public String dt;
}
