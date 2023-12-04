#pragma once
#include <ArduinoWebsockets.h>
#include <WiFiClient.h>
#include "constants.h"
#include "network.h"

#define ACTION_JSON_SIZE 256

using namespace websockets;

enum ServerAction {
    ACTION_FINISHED,
    UPDATE_DEVICE_CONFIG,
    UPDATE_DEVICE_STATE,
    REQUEST_PAIR
};

struct Param {
    String name;
    String value;
};

WebsocketsClient webSocket;

void (*onReceived)(JsonObject action);

void onEventsCallback(WebsocketsEvent event, String data) {
    if(event == WebsocketsEvent::ConnectionOpened) {
        Serial.println("Connnection Opened");
    } else if(event == WebsocketsEvent::ConnectionClosed) {
        Serial.println("Connnection Closed");
    }
}

void socketConnect() {
    if(NetworkManager::connectionAvailable()) {
        webSocket.onEvent(onEventsCallback);
        webSocket.onMessage([](WebsocketsMessage msg){
            DynamicJsonDocument doc(ACTION_JSON_SIZE);
            deserializeJson(doc, msg.data());
            onReceived(doc.as<JsonObject>());
        });
        webSocket.connect("ws://" + String(SERVER_URL) + "/websocket/beehive?token=" + String(BEEHIVE_ID));
    }
}

void onSocketActionReceived(void (*function)(JsonObject action)) {
    onReceived = function;
}

template <size_t N>
void sendActionToServer(ServerAction serverAction, Param (&params)[N]) {
    String message;
    DynamicJsonDocument doc(ACTION_JSON_SIZE);
    doc["type"] = serverAction;
    doc["params"] = doc.createNestedObject();
    for (int i = 0; i < N; i++) {
        int intValue = params[i].value.toInt();
        if (params[i].value.equals(String(intValue))) doc["params"][params[i].name] = intValue;
        else doc["params"][params[i].name] = params[i].value;       
    }
    serializeJson(doc, message);
    webSocket.send(message);
    Serial.println(message);
}

void updateSocket() {
    webSocket.poll();
}
