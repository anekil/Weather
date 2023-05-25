package com.pam.weather;

import com.pam.weather.weatherresponse.WeatherResponse;

import java.util.HashMap;

public class FavouritesData {
    static HashMap<String, WeatherResponse> favourites = new HashMap<>();
    static Units units = Units.METRIC;

    static void addFavourite(String cityName, WeatherResponse weather){
        favourites.put(cityName, weather);
    }

    static WeatherResponse getWeather(String cityName){
        return favourites.get(cityName);
    }

    static void deleteFavourite(String cityName){
        favourites.remove(cityName);
    }

    static HashMap<String, WeatherResponse> getFavourites(){
        return favourites;
    }

    static void loadData(HashMap<String, WeatherResponse> data){
        favourites = data;
    }

    static void setUnits(Units newUnits){
        units = newUnits;
    }

    public static Units getUnits(){
        return units;
    }
}
