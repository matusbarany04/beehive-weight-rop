package com.buzzybees.master.beehives.actions;

import com.buzzybees.master.beehives.devices.DeviceType;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ActionType {

    UPDATE_STATUS("UPDATE_STATUS"),
    MOTOR_MOVE("MOTOR_MOVE", DeviceType.MOTOR),

    LED_TOGGLE("LED_TOGGLE", DeviceType.LED),

    WAKE_UP("WAKE_UP", false, true),
    HIBERNATE("HIBERNATE", false, true),
    ENABLE_SHARING_CONNECTION("ENABLE_SHARING_CONNECTION", true, true),

    BURN_SENSOR_ID("BURN_SENSOR_ID", true),
    CHANGE_BEEHIVE_CONFIG("CHANGE_CONFIG", true, true),
    FACTORY_RESET("FACTORY_RESET", false, true);

    public final String name;

    private DeviceType bind = null;

    boolean systemAction = false;
    boolean singleInstance = false;

    ActionType(String string) {
        name = string;
    }

    ActionType(String string, DeviceType bind) {
        name = string;
        this.bind = bind;
    }

    ActionType(String string, boolean systemAction) {
        name = string;
        this.systemAction = systemAction;
    }

    ActionType(String string, boolean systemAction, boolean singleInstance) {
        this(string, systemAction);
        this.singleInstance = singleInstance;
    }

    ActionType() {
        name = this.toString();
    }


    /**
     *
     * @return ActionType[]
     */
    public static ActionType[] getNonSystemValues() {
        return Arrays.stream(ActionType.values()).filter((val) -> !val.systemAction).toArray(ActionType[]::new);
    }

    /**
     *
     * @return String
     */
    public String getName() {
        return name;
    }



    /**
     * Getter for bind value
     * @return boolean if ActionType has bind DeviceType present
     */
    public boolean isDeviceBound() {
        return bind != null;
    }

    /**
     * Returns bound DeviceType to the action
     *
     * @return DeviceType
     * @throws NullPointerException the bind value is not present in all the action types
     */
    public DeviceType getDeviceBoundType() throws NullPointerException {
        return bind;
    }


    /**
     * Filter enum values based on whether they are bound or not
     *
     * @return ActionType[] containing only bound values
     */
    public static ActionType[] getDeviceBoundValues() {
        return Arrays.stream(ActionType.values())
                .filter(ActionType::isDeviceBound)
                .toArray(ActionType[]::new);
    }



    /**
     * Filter enum values based on whether they are non-system and not bound
     *
     * @return ActionType[] containing only non-system and not bound values
     */
    public static ActionType[] getNonSystemNonBoundValues() {
        return Arrays.stream(ActionType.values())
                .filter((val) -> !val.systemAction && !val.isDeviceBound())
                .toArray(ActionType[]::new);
    }

    /**
     * Filter enum values based on whether they are bound and not system
     *
     * @return ActionType[] containing only bound and not system values
     */
    public static ActionType[] getBoundNonSystemValues() {
        return Arrays.stream(ActionType.values())
                .filter((val) -> val.isDeviceBound() && !val.systemAction)
                .toArray(ActionType[]::new);
    }


}

