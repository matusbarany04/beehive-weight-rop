#pragma once
#include <ArduinoWebsockets.h>
#include <WiFiClient.h>
#include "constants.h"
#include "network.h"

#define ACTION_JSON_SIZE 256
#define PING_INTERVAL 60000
#define SOCKET_TIMEOUT 15000

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
ulong lastPing;
long attemptStart;
bool socket_connecting = false;

void (*onReceived)(JsonObject action);
void socketConnect();

void onEventsCallback(WebsocketsEvent event, String data) {
    if(event == WebsocketsEvent::ConnectionOpened) {
        Serial.println("Connnection Opened");
        socket_connecting = false;
        led.indicate(POWER);

    } else if(event == WebsocketsEvent::ConnectionClosed) {
        Serial.println("Connnection Closed");
        delay(1000);
        socketConnect();
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

void listenSocketMessages() {
    webSocket.onMessage([](WebsocketsMessage msg){
        DynamicJsonDocument doc(ACTION_JSON_SIZE);
        deserializeJson(doc, msg.data());
        onReceived(doc.as<JsonObject>());
    });
}


void socketConnect() {
    socket_connecting = true;
    Serial.println(webSocket.available());
    if(NetworkManager::connectionAvailable()) {
        attemptStart = millis();
        webSocket.onEvent(onEventsCallback);
        listenSocketMessages();
        webSocket.connect("ws://" + String(SERVER_URL) + "/websocket/beehive?token=" + String(BEEHIVE_ID));
        Param params[] = {{"newState", "ONLINE"}};
        sendActionToServer(UPDATE_DEVICE_STATE, params);
    }
}

bool socketConnectionAvailable() {
    return webSocket.available();
}

void updateSocket() {
    if(socket_connecting && millis() - attemptStart > SOCKET_TIMEOUT) socketConnect(); 
    webSocket.poll();
    if(millis() - lastPing >= PING_INTERVAL) {
        webSocket.send("ping");
        lastPing = millis();
    }
}
