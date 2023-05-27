package com.pam.weather;

public enum Units {
    STANDARD ("K", "m/s"),
    METRIC ("℃", "m/s"),
    IMPERIAL ("°F", "mph");

    public final String temp_label;
    public final String speed_label;

    private Units(String temp_label, String speed_label) {
        this.temp_label = temp_label;
        this.speed_label = speed_label;
    }
}
