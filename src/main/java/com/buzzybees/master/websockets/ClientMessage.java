package com.buzzybees.master.websockets;

import java.util.HashMap;

public record ClientMessage(MessageType messageType, HashMap<String, Object> params) {
    public void putParam(String key, Object value) {
        params.put(key, value);
    }
}
