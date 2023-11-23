package com.buzzybees.master.beehives.actions;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ActionType {

    MOTOR_MOVE("MOTOR_MOVE"),

    WAKE_UP("WAKE_UP"),

    BURN_SENSOR_ID("BURN_SENSOR_ID", true);

    public final String name;

    boolean systemAction = false;

    ActionType(String string){
        name = string;
    }

    ActionType(String string, boolean systemAction){
        name = string;
        this.systemAction = systemAction;
    }

    ActionType(){
        name = this.toString();
    }

    public static ActionType[] getNonSystemValues(){
        return Arrays.stream(ActionType.values()).filter((val)-> !val.systemAction).toArray(ActionType[]::new);
    }

    public String getName() {
        return name;
    }


}

