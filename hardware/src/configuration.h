#pragma once
#include <Arduino.h>
#include <EEPROM.h>

#define ADDRESS 0
#define EEPROM_SIZE 512

struct Config {
    int interval;
    char connectionMode[15];
    char sim_password[8];
    char wifi_ssid[32];
    char wifi_password[32];
};

bool initialized = false;

void save(Config config) {
    if(!initialized) init();
    EEPROM.put(ADDRESS, config);
    EEPROM.commit();
}

void load(Config* config) {
    if(!initialized) init();
    EEPROM.get(ADDRESS, *config);
}

void init() {
    initialized = EEPROM.begin(EEPROM_SIZE);
}