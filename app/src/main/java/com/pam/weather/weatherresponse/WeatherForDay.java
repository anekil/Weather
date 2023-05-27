package com.pam.weather.weatherresponse;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherForDay {
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("visibility")
    public float visibility;
    @SerializedName("weather")
    public ArrayList<Weather> weather = new ArrayList<>();
    @SerializedName("dt")
    public long dt;

    public String getDt(){
        return new SimpleDateFormat("HH:mm dd.MM", Locale.ENGLISH).format(new Date(dt * 1000));
    }
}
