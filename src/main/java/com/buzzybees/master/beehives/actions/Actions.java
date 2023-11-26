package com.buzzybees.master.beehives.actions;

import com.buzzybees.master.beehives.Beehive;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

record ActionCallback(Beehive beehive, ActionType actionType, Actions.OnActionResponse onActionResponse) {

}


public class Actions {

    public static final int NOW = 0;
    public static final int AUTHOR_SYSTEM = 0;

    private static final ArrayList<ActionCallback> callbacks = new ArrayList<>();

    public static Action createBurnAction(Beehive beehive, long sensorId, int port) {
        return new Action(ActionType.BURN_SENSOR_ID, Actions.NOW,
                new JSONObject() {{
                    put("sensorId", sensorId);
                    put("port", port);
                }}.toString(),
                beehive.getToken(),
                Actions.AUTHOR_SYSTEM
        );
    }

    public static List<Action> createConfigActions(Beehive oldBeehive, Beehive newBeehive) {
        List<Action> actions = new LinkedList<>();
        JSONObject params = new JSONObject();

        if(oldBeehive.getInterval() != newBeehive.getInterval()) {
            params.put("interval", newBeehive.getInterval());
        }

        if(oldBeehive.getConnectionMode() != newBeehive.getConnectionMode()) {
            params.put("connectionMode", newBeehive.getConnectionMode());
        }

        switch (newBeehive.getConnectionMode()) {
            case GSM -> {
                if(newBeehive.getSim_password().length() > 0) params.put("sim_password", newBeehive.getSim_password());
            }
            case WIFI -> {
                if(!oldBeehive.getWifiSSID().equals(newBeehive.getWifiSSID())) params.put("wifi_ssid", newBeehive.getWifiSSID());
                String passwd = newBeehive.getWifi_password();
                if(passwd.length() > 0) params.put("wifi_password", passwd);
            }
            case OTHER_BEEHIVE -> actions.add(new Action(ActionType.ENABLE_SHARING_CONNECTION, null, newBeehive.getLinkedTo()));
        }

        actions.add(new Action(ActionType.CHANGE_BEEHIVE_CONFIG, params.toString(), newBeehive.getToken()));

       return actions;
    }

    public interface OnActionResponse {
        void onResponse(Action action);
    }

    public static void handleResponse(Beehive beehive, ActionType actionType, OnActionResponse callback) {
        callbacks.add(new ActionCallback(beehive, actionType, callback));
    }

    public static void invokeCallbacks(Action action) {
        for(ActionCallback callback : callbacks) {
            if(action.getBeehive().equals(callback.beehive().getToken()) && callback.actionType() == action.getType()) {
                if(action.getStatus() == ActionStatus.DONE) callback.onActionResponse().onResponse(action);
            }
        }
    }
}
