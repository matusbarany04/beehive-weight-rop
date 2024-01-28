#pragma once
#include <Arduino.h>
#include "LED.h"

#define S_TO_US_FACTOR 1000000ULL
#define CHARGE_CHECK_INTERVAL 500

enum WakeUpCause {
    COLD_BOOT,
    RTC_WAKE_UP,
    USER_EVENT,
    UNKNOWN
};

enum PowerState {
    BATTERY_POWER,
    CHARGING_IN_PROGRESS,
    CHARGING_COMPLETE
};

class Power {

    public:
    
        static long lastCheckTime;

        static void sleep(long seconds) {
            Serial.println("wake up after " + String(seconds) + " seconds");
            Serial.println("going to sleep...");
            Serial.flush();

            esp_sleep_pd_config(ESP_PD_DOMAIN_RTC_PERIPH,   ESP_PD_OPTION_OFF);
            esp_sleep_pd_config(ESP_PD_DOMAIN_RTC_SLOW_MEM, ESP_PD_OPTION_OFF);
            esp_sleep_pd_config(ESP_PD_DOMAIN_RTC_FAST_MEM, ESP_PD_OPTION_OFF);
            esp_sleep_pd_config(ESP_PD_DOMAIN_XTAL,         ESP_PD_OPTION_OFF);
            esp_sleep_enable_ext1_wakeup(WAKE_UP_PINS, ESP_EXT1_WAKEUP_ANY_HIGH);

            esp_sleep_enable_timer_wakeup(seconds * S_TO_US_FACTOR);
            esp_deep_sleep_start();
        }


        static WakeUpCause getWakeUpCause() {
            switch(esp_sleep_get_wakeup_cause()) {
                case ESP_SLEEP_WAKEUP_UNDEFINED: return COLD_BOOT;
                case ESP_SLEEP_WAKEUP_TIMER: return RTC_WAKE_UP;
                case ESP_SLEEP_WAKEUP_EXT1: return USER_EVENT;
                default: return UNKNOWN;
            }
        }

        static PowerState getState() {
            lastCheckTime = millis();
            if(digitalRead(CHARGING_PIN)) return CHARGING_IN_PROGRESS;
            else return BATTERY_POWER;
        }
};

long Power::lastCheckTime = 0;