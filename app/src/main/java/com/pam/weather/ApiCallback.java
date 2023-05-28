package com.pam.weather;


public interface ApiCallback {
    void onApiResponseAll(boolean received);
    void onApiResponseCurrent(boolean received);
    void onApiFailure(Throwable t);
}