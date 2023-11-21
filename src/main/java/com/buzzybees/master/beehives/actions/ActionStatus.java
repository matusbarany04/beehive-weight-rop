package com.buzzybees.master.beehives.actions;

public enum ActionStatus {

    DONE("DONE"),
    PENDING("PENDING"),
    DEVICE_NOT_FOUND("DEVICE_NOT_FOUND"),
    BEEHIVE_IS_OFFLINE("BEEHIVE_IS_OFFLINE");

    public final String name;

    ActionStatus(String string){
        name = string;
    }

    public String getName() {
        return name;
    }
}
