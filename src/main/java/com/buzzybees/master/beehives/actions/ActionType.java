package com.buzzybees.master.beehives.actions;

import com.buzzybees.master.beehives.devices.DeviceType;
import com.buzzybees.master.utils.json.JSONForm;
import com.buzzybees.master.utils.json.JSONParam;
import com.buzzybees.master.utils.json.ParamType;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ActionType {
    UPDATE_STATUS("UPDATE_STATUS",
            new JSONForm()
    ),
    MOTOR_MOVE("MOTOR_MOVE", DeviceType.MOTOR,
            new JSONForm()
                    .addParam(new JSONParam("degrees", ParamType.NUMERIC, true))
    ),
    LED_TOGGLE("LED_TOGGLE", DeviceType.LED,
            new JSONForm()
                    .addParam(new JSONParam("status", ParamType.BOOLEAN, true))
    ),
    WAKE_UP("WAKE_UP", false, true,
            new JSONForm()
    ),
    HIBERNATE("HIBERNATE", false, true,
            new JSONForm()
    ),
    ENABLE_SHARING_CONNECTION("ENABLE_SHARING_CONNECTION", true, true,
            new JSONForm()
    ),
    BURN_SENSOR_ID("BURN_SENSOR_ID", true,
            new JSONForm()
                    .addParam(new JSONParam("sensorId", ParamType.STRING, true))
                    .addParam(new JSONParam("port", ParamType.STRING, true))
    ),
    // TODO change JSONForm() to something meaningful because it wont work without it now :)
    CHANGE_BEEHIVE_CONFIG("CHANGE_CONFIG", true, true,
            new JSONForm()
    ),
    FACTORY_RESET("FACTORY_RESET", false, true,
            new JSONForm()
    ),

    CALIBRATE_WEIGHT_SENSORS("CALIBRATE_WEIGHT_SENSORS", false, true, new JSONForm().addParam(new JSONParam("weight", ParamType.NUMERIC, true))),

    UNPAIR("UNPAIR",true,true, new JSONForm());

    public final String name;

    private DeviceType bind = null;

    boolean systemAction = false;
    boolean singleInstance = false;


    private JSONForm paramForm;

    ActionType(String string, JSONForm paramForm) {
        this.paramForm = paramForm;
        name = string;
    }


    public boolean isInValidForm(JSONObject jsonObject){
        if(paramForm.getParams().size() == 0) return true; //docasne som to dal tu aby mi to fungovalo potom to mozes vymazat ak to nebude treba

        paramForm.addParam(new JSONParam("sensorId", ParamType.NUMERIC, true));
        boolean validForm = paramForm.isInValidForm(jsonObject);
        paramForm.popParam();
        return validForm;
    }

    ActionType(String string, DeviceType bind, JSONForm paramForm) {
        this.paramForm = paramForm;
        name = string;
        this.bind = bind;
    }

    ActionType(String string, boolean systemAction, JSONForm paramForm) {
        this.paramForm = paramForm;
        name = string;
        this.systemAction = systemAction;
    }

    ActionType(String string, boolean systemAction, boolean singleInstance, JSONForm paramForm) {

        this(string, systemAction, paramForm);
        this.singleInstance = singleInstance;
    }

    ActionType() {
        name = this.toString();
    }


    /**
     * @return ActionType[]
     */
    public static ActionType[] getNonSystemValues() {
        return Arrays.stream(ActionType.values()).filter((val) -> !val.systemAction).toArray(ActionType[]::new);
    }

    /**
     * @return String
     */
    public String getName() {
        return name;
    }


    /**
     * Getter for bind value
     *
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


    public JSONForm getParamForm() {
        return paramForm;
    }


}

