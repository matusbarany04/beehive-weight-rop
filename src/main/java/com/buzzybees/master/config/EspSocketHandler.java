package com.buzzybees.master.config;

import com.buzzybees.master.beehives.Beehive;
import com.buzzybees.master.beehives.BeehiveRepository;
import com.buzzybees.master.beehives.actions.Action;
import com.buzzybees.master.beehives.actions.ActionRepository;
import com.buzzybees.master.beehives.actions.ServerActionType;
import com.buzzybees.master.beehives.devices.DeviceManager;
import com.buzzybees.master.controllers.template.DatabaseController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;

record ServerAction(ServerActionType type, HashMap<String, Object> params) {

}


public class EspSocketHandler extends TextWebSocketHandler {

    private static final HashMap<String, WebSocketSession> beehiveSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        ActionRepository actionRepository = DatabaseController.accessRepo(Action.class);
        String token = session.getAttributes().get("beehive").toString();
        beehiveSessions.put(token, session);
        Action[] actions = actionRepository.getPendingActionsByBeehiveId(token);

        for(Action action : actions) {
            String json = new ObjectMapper().writeValueAsString(action);
            session.sendMessage(new TextMessage(json));
        }
    }

    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message) throws Exception {
        String payload = message.getPayload().toString();
        BeehiveRepository beehiveRepository = DatabaseController.accessRepo(Beehive.class);
        Beehive beehive = beehiveRepository.getBeehiveByToken(session.getAttributes().get("beehive").toString());
        executeServerAction(beehive, new ObjectMapper().readValue(payload, ServerAction.class));
    }

    public static void sendFlashActionToBeehive(String beehiveToken, Action action) {
        try {
            WebSocketSession session = beehiveSessions.get(beehiveToken);
            String json = new ObjectMapper().writeValueAsString(action);
            session.sendMessage(new TextMessage(json));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) throws Exception {
        String token = session.getAttributes().get("beehive").toString();
        if (token != null) beehiveSessions.remove(token);
    }

    private void executeServerAction(Beehive beehive, ServerAction serverAction) {
        switch (serverAction.type()) {
            case UPDATE_DEVICE_CONFIG -> DeviceManager.updateDeviceConfig(beehive, serverAction.params());
            default -> System.out.println("unknown server action: " + serverAction.type());
        }
    }
}
