#pragma once
#include <Arduino.h>
#include <EEPROM.h>

#include "enums.h"

#define EEPROM_SIZE 512
#define CONFIG_ADDRESS 0
#define TIMESTAMP_ADDRESS sizeof(Config) 
#define ACTIONS_ADDRESS TIMESTAMP_ADDRESS + sizeof(long)

struct Config {
    int interval;
    ConnectionMode connectionMode;
    char sim_password[8];
    char wifi_ssid[32];
    char wifi_password[32];
};

struct ScheduledAction {
    long id;
    ActionType type;
    char params[120];
    long executionTime;
};

bool initialized = false;

void save(Config config) {
    if(!initialized) init();
    EEPROM.put(CONFIG_ADDRESS, config);
    EEPROM.commit();
}

void load(Config* config) {
    if(!initialized) init();
    EEPROM.get(CONFIG_ADDRESS, *config);
}

void init() {
    initialized = EEPROM.begin(EEPROM_SIZE);
}

bool saveAction(ScheduledAction action) {
    uint8_t actionCount;
    EEPROM.get(ACTIONS_ADDRESS, actionCount);
    Serial.print("action count: ");
    Serial.println(actionCount);
    if(ACTIONS_ADDRESS + sizeof(actionCount) + (actionCount + 1) * sizeof(ScheduledAction) >= EEPROM_SIZE) return false;

    EEPROM.put(ACTIONS_ADDRESS, actionCount + 1);
    EEPROM.put(ACTIONS_ADDRESS + 1 + actionCount * sizeof(ScheduledAction), action);
    EEPROM.commit();
    actionCount++;

    return true;
}

void deleteAction(ScheduledAction* action) {
    uint8_t actionCount;
    EEPROM.get(ACTIONS_ADDRESS, actionCount);

    int foundIndex = -1;
    for (int i = 1; i < actionCount * sizeof(ScheduledAction) + 1; i += sizeof(ScheduledAction)) {
        ScheduledAction savedAction;
        EEPROM.get(ACTIONS_ADDRESS + i, savedAction);

        if(savedAction.id == action->id) foundIndex = i;
        else if(foundIndex != -1) EEPROM.put(ACTIONS_ADDRESS + i - sizeof(ScheduledAction), savedAction);
    }

    EEPROM.put(ACTIONS_ADDRESS, actionCount - 1);
    EEPROM.commit();
}

ScheduledAction** loadScheduledActions(uint8_t* actionCount) {
    EEPROM.get(ACTIONS_ADDRESS, *actionCount);

    ScheduledAction** actions = (ScheduledAction**) malloc(*actionCount * sizeof(ScheduledAction*));
    for (int i = 0; i < *actionCount; i++) {
        ScheduledAction action;
        EEPROM.get(ACTIONS_ADDRESS + i * sizeof(ScheduledAction) + 1, action);
        Serial.print("addr: ");
        Serial.println(ACTIONS_ADDRESS + i * sizeof(ScheduledAction) + 1);
        actions[i] = (ScheduledAction*) malloc(sizeof(ScheduledAction));
        *(actions[i]) = action;
    }

    return actions;
}

void factoryReset() {
    Config newConfig;
    newConfig.interval = 30;
    newConfig.connectionMode = WIFI;
    memcpy(newConfig.wifi_ssid, "SNPD", sizeof(newConfig.wifi_ssid));
    memcpy(newConfig.wifi_password, "ke257-NT_61_ab", sizeof(newConfig.wifi_password));
    save(newConfig);
    EEPROM.put(ACTIONS_ADDRESS, 0);
    EEPROM.commit();
}

void saveNextTime(long timestamp) {
    EEPROM.put(TIMESTAMP_ADDRESS, timestamp);
    EEPROM.commit();
}

long getSavedTime() {
    long timestamp;
    EEPROM.get(TIMESTAMP_ADDRESS, timestamp);
    return timestamp;
}