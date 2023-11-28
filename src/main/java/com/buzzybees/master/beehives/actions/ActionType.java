package com.buzzybees.master.beehives.actions;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ActionType {

    UPDATE_STATUS("UPDATE_STATUS"),
    MOTOR_MOVE("MOTOR_MOVE"),

    WAKE_UP("WAKE_UP", false, true),
    HIBERNATE("HIBERNATE", false, true),
    ENABLE_SHARING_CONNECTION("ENABLE_SHARING_CONNECTION", true, true),

    BURN_SENSOR_ID("BURN_SENSOR_ID", true),
    CHANGE_BEEHIVE_CONFIG("CHANGE_CONFIG", true, true),
    FACTORY_RESET("FACTORY_RESET", false, true);

    public final String name;

    boolean systemAction = false;
    boolean singleInstance = false;

    ActionType(String string){
        name = string;
    }

    ActionType(String string, boolean systemAction){
        name = string;
        this.systemAction = systemAction;
    }

    ActionType(String string, boolean systemAction, boolean singleInstance){
        this(string, systemAction);
        this.singleInstance = singleInstance;
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

