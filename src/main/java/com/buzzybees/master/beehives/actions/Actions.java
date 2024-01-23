package com.buzzybees.master.beehives.actions;

import com.buzzybees.master.beehives.Beehive;
import com.buzzybees.master.beehives.BeehiveRepository;
import com.buzzybees.master.controllers.template.DatabaseController;
import com.buzzybees.master.notifications.Notifications;
import com.buzzybees.master.websockets.ClientMessage;
import com.buzzybees.master.websockets.ClientSocketHandler;
import com.buzzybees.master.websockets.MessageType;
import com.buzzybees.master.websockets.ServerAction;
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
                String oldSSID = oldBeehive.getWifiSSID();
                if(oldSSID == null || !oldSSID.equals(newBeehive.getWifiSSID())) params.put("wifi_ssid", newBeehive.getWifiSSID());
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

    public static Action updateActionStatus(long actionID, ActionStatus newStatus) throws IllegalArgumentException, NullPointerException {
        ActionRepository actionRepository = DatabaseController.accessRepo(Action.class);
        Action action = actionRepository.getActionById(actionID);

        action.setStatus(newStatus);

        Actions.invokeCallbacks(action);
        Action result = actionRepository.save(action);

        if (newStatus != ActionStatus.DONE) Notifications.errorAlert(action);
        return result;
    }

    public static void sendActionLogToUser(ServerAction serverAction) {
        Number actionID = (Number) serverAction.params().get("id");
        ActionStatus newStatus = ActionStatus.valueOf((String) serverAction.params().get("status"));
        Action action = updateActionStatus(actionID.longValue(), newStatus);

        String token = action.getBeehive();
        long author = action.getAuthor();

        if(author == AUTHOR_SYSTEM) {
            BeehiveRepository beehiveRepository = DatabaseController.accessRepo(Beehive.class);
            Beehive beehive = beehiveRepository.getBeehiveByToken(token);
            author = beehive.getUserId();
        }

        ClientMessage clientMessage = new ClientMessage(MessageType.ACTION_RESULT, serverAction.params());
        ClientSocketHandler.sendMessageToUser(author, clientMessage);
    }
}
