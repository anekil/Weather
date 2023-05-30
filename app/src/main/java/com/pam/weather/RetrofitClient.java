package com.pam.weather;

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
        if(city == null || city.equals("") || city.equals(" ")){
            callback.onCityNotFound(city); return;
        }

        WeatherApiService apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getCurrentWeatherData(city, "25", units.name(), API_KEY);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                switch (response.code()){
                    case 401: case 429: case 500: case 502: case 503: case 504:
                        callback.onApiFailure(); return;
                    case 404:
                        callback.onCityNotFound(city); return;
                    default:
                        break;
                }

                if (response.isSuccessful()) {
                    WeatherResponse weather = response.body();
                    if (weather != null) {
                        weather.units = units;
                        FavouritesManager.addFavourite(city, prepareResponse(weather));
                        callback.onApiResponseAll();
                    } else {
                        callback.onApiFailure();
                    }
                } else {
                    callback.onApiFailure();
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onApiFailure();
            }
        });
    }

    public void updateCurrent(ApiCallback callback) {
        if(FavouritesManager.currentCity.equals("")){
            callback.onCityNotFound(FavouritesManager.currentCity); return;
        }

        WeatherApiService apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getCurrentWeatherData(FavouritesManager.currentCity, "25", FavouritesManager.currentUnits.name(), API_KEY);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                switch (response.code()){
                    case 401: case 429: case 500: case 502: case 503: case 504:
                        callback.onApiFailure(); return;
                    case 404:
                        callback.onCityNotFound(FavouritesManager.currentCity); return;
                    default:
                        break;
                }

                if (response.isSuccessful()) {
                    WeatherResponse weather = response.body();
                    if (weather != null) {
                        weather.units = FavouritesManager.currentUnits;
                        FavouritesManager.currentWeather = prepareResponse(weather);
                        callback.onApiResponseCurrent();
                    } else {
                        callback.onApiFailure();
                    }
                } else {
                    callback.onApiFailure();
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onApiFailure();
            }
        });
    }

    WeatherResponse prepareResponse(WeatherResponse weather){
        ArrayList<WeatherForDay> days = new ArrayList<>();
        for (int i = 0; i <= 24; i += 8) {
            days.add(weather.list.get(i));
        }
        weather.list = days;
        weather.list.get(0).dt = Instant.now().getEpochSecond();
        return weather;
    }
}
