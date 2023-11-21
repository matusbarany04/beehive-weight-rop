package com.buzzybees.master.beehives;

public enum BeehiveActions {
    MOVE_MOTOR("MOVE_MOTOR"),

    BURN_SENSOR_ID("BURN_SENSOR_ID");

    public final String name;
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
