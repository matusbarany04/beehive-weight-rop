#pragma once
#include <Arduino.h>

#define IDLE 0
#define OK 1
#define REQUEST_SUCCESS 2
#define CONNECTING 3
#define ERROR -1

#define TASK_NAME "blink"

struct Params {
    int duration;
    uint8_t times;
    bool infinite;
    uint8_t pin;
};

class LED {

    public:

        LED(uint8_t pin) {
            this->pin = pin;
            pinMode(pin, OUTPUT);
            digitalWrite(this->pin, HIGH);
        }

        void indicate(int status) {
            endProcess();
            Params params;

            switch (status) {
            
                case OK:
                    on();
                    break;

                case IDLE:
                    params = {250, 3, false, this->pin}; 
                    xTaskCreate(blinkLoop, TASK_NAME, configMINIMAL_STACK_SIZE, &params, 1, &blinkTask);
                    break;

                case REQUEST_SUCCESS:
                    params = {250, 2, false, this->pin}; 
                    xTaskCreate(blinkLoop, TASK_NAME, configMINIMAL_STACK_SIZE, &params, 1, &blinkTask);
                    break;

                case CONNECTING:
                    params = {500, 1, true, this->pin}; 
                    xTaskCreate(blinkLoop, TASK_NAME, configMINIMAL_STACK_SIZE, &params, 1, &blinkTask);
                    break;

                case ERROR:
                    params = {250, 2, true, this->pin};
                    xTaskCreate(blinkLoop, TASK_NAME, configMINIMAL_STACK_SIZE, &params, 1, &blinkTask);
           } 
        }

        static void blinkLoop(void *pvParameters) {
            Params* params = (Params*) pvParameters;

            do {
                for(int i = 0; i < params->times; i++) blinkOnce(params);
                vTaskDelay(params->duration / portTICK_RATE_MS * (params->times - 1));

            } while(params->infinite);
            while(true);
        }

        static void blinkOnce(Params* params) {
            digitalWrite(params->pin, LOW);
            vTaskDelay(params->duration / portTICK_RATE_MS);
            digitalWrite(params->pin, HIGH);
            vTaskDelay(params->duration / portTICK_RATE_MS);
        }

        void endProcess() {
            if(blinkTask != NULL) vTaskDelete(blinkTask);
            blinkTask = NULL;
        }

        void off() {
            endProcess();
            digitalWrite(this->pin, HIGH);
        }

        void on() {
            endProcess();
            digitalWrite(this->pin, LOW);
        }
        
    private:
        uint8_t pin;
        TaskHandle_t blinkTask;
};