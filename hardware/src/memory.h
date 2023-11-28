#pragma once
#include <Arduino.h>
#include <EEPROM.h>

#include "enums.h"

#define EEPROM_SIZE 512
#define CONFIG_ADDRESS 0
#define ACTIONS_ADDRESS sizeof(Config)

struct Config {
    int interval;
    ConnectionMode connectionMode;
    char sim_password[8];
    char wifi_ssid[32];
    char wifi_password[32];
};

struct ScheduledAction {
    ActionType actionType;
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
    actionCount++;
    if((actionCount) * sizeof(ScheduledAction) + sizeof(Config) >= EEPROM_SIZE) return false;

    EEPROM.put(ACTIONS_ADDRESS, actionCount);
    EEPROM.put(ACTIONS_ADDRESS + actionCount, action);
    EEPROM.commit();
    return true;
}

int loadScheduledActions(ScheduledAction** actions) {
    uint8_t actionCount;
    EEPROM.get(ACTIONS_ADDRESS, actionCount);

    actions = (ScheduledAction**) malloc(actionCount * sizeof(ScheduledAction*));
    for (int i = 1; i <= actionCount; i++) {
        EEPROM.get(ACTIONS_ADDRESS + i, *actions[i]);
    }

    return actionCount;
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