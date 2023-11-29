package com.buzzybees.master.beehives.actions;

public enum ActionStatus {

    DONE("DONE"),
    PENDING("PENDING"),
    SCHEDULED("SCHEDULED"),
    SENT("SENT"),
    WIFI_CONNECTION_FAILED("WIFI_CONNECTION_FAILED"),
    WIFI_NO_SSID_AVAIL("WIFI_NO_SSID_AVAIL"),
    DEVICE_NOT_FOUND("DEVICE_NOT_FOUND"),
    ESP_NOT_ENOUGH_MEMORY("ESP_NOT_ENOUGH_MEMORY"),
    UNKNOWN_ERROR("UNKNOWN_ERROR"),
    BEEHIVE_IS_OFFLINE("BEEHIVE_IS_OFFLINE");

    public final String name;

    ActionStatus(String string){
        name = string;
    }

    public String getName() {
        return name;
    }
}
