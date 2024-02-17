#pragma once

enum ActionType {
    UPDATE_STATUS,
    MOTOR_MOVE,
    WAKE_UP,
    HIBERNATE,
    ENABLE_SHARING_CONNECTION,
    BURN_SENSOR_ID,
    CHANGE_BEEHIVE_CONFIG,
    FACTORY_RESET,
    CALIBRATE_WEIGHT_SENSORS,
    UNPAIR
};

enum ConnectionMode {
    GSM,
    WIFI,
    OTHER_BEEHIVE
};


ActionType parseActionType(const char* enumString) {
    const char* actionTypeStrings[] = {"UPDATE_STATUS", "MOTOR_MOVE", "WAKE_UP", "HIBERNATE", "ENABLE_SHARING_CONNECTION", "BURN_SENSOR_ID", "CHANGE_BEEHIVE_CONFIG", "FACTORY_RESET", "CALIBRATE_WEIGHT_SENSORS", "UNPAIR"};

    for (int i = 0; i < sizeof(actionTypeStrings) / sizeof(actionTypeStrings[0]); ++i) {
        if (strcmp(enumString, actionTypeStrings[i]) == 0) return (enum ActionType)i;
    }

    return (ActionType)0;
}

ConnectionMode parseConnectionMode(const char* enumString) {
    const char* modes[] = {"GSM", "WIFI", "OTHER_BEEHIVE"};

    for (int i = 0; i < sizeof(modes) / sizeof(modes[0]); ++i) {
        if (strcmp(enumString, modes[i]) == 0) return (enum ConnectionMode)i;
    }

    return (ConnectionMode)0;
}