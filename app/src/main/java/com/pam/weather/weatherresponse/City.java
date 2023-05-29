package com.pam.weather.weatherresponse;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class City {
    @SerializedName("name")
    public String name;
    @SerializedName("coord")
    public Coord coord;
    @SerializedName("country")
    public String country;
    @SerializedName("sunrise")
    public long sunrise;
    @SerializedName("sunset")
    public long sunset;

    public String getSunrise(){
        return new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(sunrise * 1000));
    }

    public String getSunset(){
        return new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(sunset * 1000));
    }
}
