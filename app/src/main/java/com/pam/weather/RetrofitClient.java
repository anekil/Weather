package com.pam.weather;

import android.content.Intent;

import com.pam.weather.weatherresponse.WeatherForDay;
import com.pam.weather.weatherresponse.WeatherResponse;

import java.time.Instant;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String API_KEY = "a7801ab3bb1ab1a6e70f97bb4b575006";
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public void updateFavourite(ApiCallback callback, String city, Units units) {
        WeatherApiService apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getCurrentWeatherData(city, "25", units.name(), API_KEY);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weather = response.body();
                    if (weather != null) {
                        weather.units = units;
                        ArrayList<WeatherForDay> days = new ArrayList<>();
                        for(int i=0; i<=24; i+=8){
                            days.add(weather.list.get(i));
                        }
                        weather.list = days;
                        weather.list.get(0).dt = Instant.now().getEpochSecond();
                        FavouritesManager.addFavourite(city, weather);
                        callback.onApiResponseAll(true);
                    } else {
                        callback.onApiResponseAll(false);
                    }
                } else {
                    callback.onApiResponseAll(false);
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onApiFailure(t);
            }
        });
    }

    public void updateCurrent(ApiCallback callback) {
        WeatherApiService apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getCurrentWeatherData(FavouritesManager.currentCity, "25", FavouritesManager.currentUnits.name(), API_KEY);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weather = response.body();
                    if (weather != null) {
                        weather.units = FavouritesManager.currentUnits;
                        ArrayList<WeatherForDay> days = new ArrayList<>();
                        for (int i = 0; i <= 24; i += 8) {
                            days.add(weather.list.get(i));
                        }
                        weather.list = days;
                        weather.list.get(0).dt = Instant.now().getEpochSecond();
                        FavouritesManager.currentWeather = weather;
                        callback.onApiResponseCurrent(true);
                    } else {
                        callback.onApiResponseAll(false);
                    }
                } else {
                    callback.onApiResponseAll(false);
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onApiFailure(t);
            }
        });
    }
}
