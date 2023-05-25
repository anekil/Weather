package com.pam.weather.weatherresponse;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("name")
    public String name;
    @SerializedName("coord")
    public Coord coord;
    @SerializedName("sunrise")
    public long sunrise;
    @SerializedName("sunset")
    public long sunset;
}
