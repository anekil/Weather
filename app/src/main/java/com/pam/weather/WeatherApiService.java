package com.pam.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("data/2.5/weather?")
    Call<WeatherResponse> getCurrentWeatherData(@Query("q") String city,@Query("cnt") String days, @Query("units") String units,  @Query("APPID") String app_id);
}
