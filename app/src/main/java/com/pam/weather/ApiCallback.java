package com.pam.weather;


public interface ApiCallback {
    void onApiResponseAll();
    void onApiResponseCurrent();
    void onCityNotFound(String city);
    void onApiFailure();
}