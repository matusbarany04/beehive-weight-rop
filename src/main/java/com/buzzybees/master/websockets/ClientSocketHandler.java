package com.buzzybees.master.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;

public class ClientSocketHandler extends TextWebSocketHandler {

    private static final HashMap<Long, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) {
        System.out.println(session.getAttributes());
        System.out.println(sessions);
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        long userID = (long) session.getAttributes().get("userID");
        sessions.put(userID, session);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        long userID = (long) session.getAttributes().get("userID");
        sessions.remove(userID);
        System.out.println("connection closed for session: " + session);
    }

    public static WebSocketSession getSession(long userID) {
        return sessions.get(userID);
    }

    public static void sendMessageToUser(long userID, ClientMessage clientMessage) {
        try {
            String json = new ObjectMapper().writeValueAsString(clientMessage);
            WebSocketSession userSession = getSession(userID);
            if(userSession != null) userSession.sendMessage(new TextMessage(json));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
