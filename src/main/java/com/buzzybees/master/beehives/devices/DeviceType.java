package com.buzzybees.master.beehives.devices;

public enum DeviceType {
    TEMPERATURE("temperature"),
    HUMIDITY("humidity"),
    LIGHT("light"),
    SOUND("sound"),
    MOTOR(null);

    private final String listName;

    DeviceType(String listName) {
        this.listName = listName;
    }

    public String getListName() {
        return listName;
    }
}
