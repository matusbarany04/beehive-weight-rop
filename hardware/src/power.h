#pragma once
#include <Arduino.h>
#include "LED.h"

#define S_TO_US_FACTOR 1000000ULL

enum WakeUpCause {
    COLD_BOOT,
    DEEP_SLEEP_WAKE_UP,
    UNKNOWN
};

class Power {

    public:

        static void sleep(long seconds) {
            Serial.println("wake up after " + String(seconds) + " seconds");
            Serial.println("going to sleep...");
            Serial.flush();

            esp_sleep_pd_config(ESP_PD_DOMAIN_RTC_PERIPH,   ESP_PD_OPTION_OFF);
            esp_sleep_pd_config(ESP_PD_DOMAIN_RTC_SLOW_MEM, ESP_PD_OPTION_OFF);
            esp_sleep_pd_config(ESP_PD_DOMAIN_RTC_FAST_MEM, ESP_PD_OPTION_OFF);
            esp_sleep_pd_config(ESP_PD_DOMAIN_XTAL,         ESP_PD_OPTION_OFF);

            esp_sleep_enable_timer_wakeup(seconds * S_TO_US_FACTOR);
            esp_deep_sleep_start();
        }


        static WakeUpCause getWakeUpCause() {
            switch(esp_sleep_get_wakeup_cause()) {
                case ESP_SLEEP_WAKEUP_UNDEFINED: return COLD_BOOT;
                case ESP_SLEEP_WAKEUP_TIMER: return DEEP_SLEEP_WAKE_UP;
                default: return UNKNOWN;
            }
        }

};