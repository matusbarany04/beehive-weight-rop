#pragma once
#include <Arduino.h>
#include <EEPROM.h>

struct Config {
    int interval;
    String connectionMode;
    String sim_password;
    String wifi_ssid;
    String wifi_password;
};

void save(Config config) {
    EEPROM.put(0x01, config);
}

void load(Config config) {
    EEPROM.get(0x01, config);
}