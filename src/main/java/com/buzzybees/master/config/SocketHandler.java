package com.buzzybees.master.config;

import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class SocketHandler extends TextWebSocketHandler {

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

    public static WebSocketSession getSession(long userID) {
        return sessions.get(userID);
    }
}
