package com.buzzybees.master.notifications;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class SocketTextHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {

        String payload = message.getPayload();
        System.out.println(payload);
        JSONObject jsonObject = new JSONObject(payload);
        session.sendMessage(new TextMessage("Hi " + jsonObject.get("user") + " how may we help you?"));
    }

}
