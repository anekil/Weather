package com.pam.weather;

import com.pam.weather.weatherresponse.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("forecast?")
    Call<WeatherResponse> getCurrentWeatherData(@Query("q") String city, @Query("cnt") String days, @Query("units") String units, @Query("APPID") String app_id);
}
