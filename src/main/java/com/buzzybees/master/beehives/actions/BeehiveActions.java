package com.buzzybees.master.beehives.actions;

public enum BeehiveActions {

    MOTOR_MOVE("MOTOR_MOVE"),

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
