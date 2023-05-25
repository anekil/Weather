package com.pam.weather;

public enum Units {
    STANDARD (" K"),
    METRIC (" ℃"),
    IMPERIAL (" °F");

    public final String label;

    private Units(String label) {
        this.label = label;
    }
}
