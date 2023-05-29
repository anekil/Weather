package com.pam.weather;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pam.weather.weatherresponse.WeatherResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public final class FavouritesManager {
    static RetrofitClient retrofitClient;
    private static volatile FavouritesManager instance;
    static HashMap<String, WeatherResponse> favourites;
    static String currentCity;
    static WeatherResponse currentWeather = null;
    static Units currentUnits;
    static SharedPreferences sharedPreferences;
    static ApiCallback currentCallback;

    private FavouritesManager() {
        favourites = new HashMap<>();
        retrofitClient = new RetrofitClient();
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

    static void setCurrentCallback(ApiCallback callback){
        currentCallback = callback;
    }
    static void setSharedPreferences(Context context){
        sharedPreferences = context.getSharedPreferences("weather", Context.MODE_PRIVATE);
    }

    static void addFavourite(String cityName, WeatherResponse weather){
        favourites.put(cityName, weather);
    }

    static void deleteFavourite(String cityName){
        favourites.remove(cityName);
    }

    static void loadData(HashMap<String, WeatherResponse> data){
        favourites = data;
    }

    static ArrayList<String> loadFavouritesList(){
        return new ArrayList<>(favourites.keySet());
    }

    static void clearAll(){
        favourites.clear();
        sharedPreferences.edit().clear().commit();
    }

    static void refresh(String city, Units units){
        retrofitClient.updateFavourite(currentCallback, city, units);
    }

    static void refreshCurrent(){
        retrofitClient.updateCurrent(currentCallback);
    }

    static boolean loadCurrent(){
        if (favourites.containsKey(currentCity) && favourites.get(currentCity) != null){
            currentWeather = favourites.get(currentCity);
            currentUnits = currentWeather.units;
            return true;
        }
        return false;
    }

    static void refreshAll(){
        for (Map.Entry<String, WeatherResponse> entry : favourites.entrySet()){
            if(entry.getValue() != null)
                refresh(entry.getKey(), entry.getValue().units);
            else
                refresh(entry.getKey(), Units.STANDARD);
        }
        saveAll();
    }

    static void saveAll(){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favourites);
        prefsEditor.putString("favourites", json);
        prefsEditor.commit();
    }
    static void loadAll(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favourites", null);
        if(json != null){
            Type type = new TypeToken<HashMap<String, WeatherResponse>>(){}.getType();
            HashMap<String, WeatherResponse> data = gson.fromJson(json, type);
            loadData(data);
        }
    }
}
