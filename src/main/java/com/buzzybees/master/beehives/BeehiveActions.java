package com.buzzybees.master.beehives;

public enum BeehiveActions {

    MOVE_MOTOR("MOVE_MOTOR"),

    WAKE_UP("WAKE_UP"),

    BURN_SENSOR_ID("BURN_SENSOR_ID");

    public final String name;

    public static final int NOW = 0;

    BeehiveActions(String string){
        name = string;
    }

    BeehiveActions(){
        name = this.toString();
    }

    public String getName() {
        return name;
    }

}
