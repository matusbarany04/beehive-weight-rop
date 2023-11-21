package com.buzzybees.master.beehives;

import java.util.ArrayList;
import java.util.HashMap;

public class Action {

    private final BeehiveActions actionName;
    private long time = BeehiveActions.NOW;
    private HashMap<String, Object> params;

    private static HashMap<String, Action> savedActions = new HashMap<>();

    public Action(BeehiveActions actionName) {
        this.actionName = actionName;
    }

    public Action(BeehiveActions actionName, long time) {
        this(actionName);
        this.time = time;
    }

    public Action(BeehiveActions actionName, long time, HashMap<String, Object> params) {
        this(actionName, time);
        this.params = params;
    }

    public String getName() {
        return actionName.name;
    }

    public long getTime() {
        return time;
    }
}
