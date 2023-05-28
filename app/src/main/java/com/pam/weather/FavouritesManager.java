package com.pam.weather;

import com.pam.weather.weatherresponse.WeatherResponse;

import java.util.HashMap;


public final class FavouritesManager {

    private static volatile FavouritesManager instance;
    static HashMap<String, WeatherResponse> favourites;
    static String currentCity;
    static WeatherResponse currentWeather;

    private FavouritesManager() {
        favourites = new HashMap<>();
    }

    public static FavouritesManager getInstance() {
        FavouritesManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(FavouritesManager.class) {
            if (instance == null) {
                instance = new FavouritesManager();
            }
            return instance;
        }
    }

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
}
