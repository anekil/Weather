package com.pam.weather;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherResponse {
    @SerializedName("coord")
    public Coord coord;

    @SerializedName("weather")
    public ArrayList<Weather> weather = new ArrayList<>();
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("visibility")
    public float visibility;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("dt")
    public int dt;
    @SerializedName("name")
    public String name;
    @SerializedName("cod")
    public float cod;
}

class Coord {
    @SerializedName("lon")
    public float lon;
    @SerializedName("lat")
    public float lat;
}

class Weather {
    @SerializedName("main")
    public String main;
    @SerializedName("description")
    public String description;
}

class Main {
    @SerializedName("temp")
    public float temp;
    @SerializedName("feels_like")
    public float feels_like;
    @SerializedName("temp_min")
    public float temp_min;
    @SerializedName("temp_max")
    public float temp_max;
    @SerializedName("humidity")
    public float humidity;
    @SerializedName("pressure")
    public float pressure;
}

class Wind {
    @SerializedName("speed")
    public float speed;
}

class Sys {
    @SerializedName("country")
    public String country;
    @SerializedName("sunrise")
    public long sunrise;
    @SerializedName("sunset")
    public long sunset;
}


