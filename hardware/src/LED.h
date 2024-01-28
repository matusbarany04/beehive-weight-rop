#pragma once
#include <Arduino.h>

enum State {
    POWER,
    IDLE,
    REQUEST_SUCCESS,
    CONNECTING,
    CHARGING,
    ERROR
};

#define TASK_NAME "blink"
#define STACK_SIZE 128
#define INFINITE -1

struct Params {
    int duration;
    int8_t times;
    bool fadeIn;
};

class LED {

    public:

        LED(uint8_t pin) {
            this->pin = pin;
            pinMode(pin, OUTPUT);
            analogWrite(this->pin, 255);
        }

        void indicate(State state) {
            if(state == currentState) return;
            currentState = state;
            Serial.print("new indication: ");
            Serial.println(state);
            endProcess();
            //Params params;

            switch (state) {
            
                case POWER:
                    on();
                    break;

               /* case IDLE:
                    params = {250, 3, false, false, this->pin}; 
                    xTaskCreate(blinkLoop, TASK_NAME, configMINIMAL_STACK_SIZE + STACK_SIZE, &params, 1, &blinkTask);
                    break;

                case REQUEST_SUCCESS:
                    params = {250, 2, false, false, this->pin}; 
                    xTaskCreate(blinkLoop, TASK_NAME, configMINIMAL_STACK_SIZE + STACK_SIZE, &params, 1, &blinkTask);
                    break;*/

                case CONNECTING:
                    params = {500, INFINITE, false}; 
                    xTaskCreate(blinkLoop, TASK_NAME, configMINIMAL_STACK_SIZE + STACK_SIZE, &params, 4, &blinkTask);
                    break;

                case ERROR:
                    params = {250, 2, false};
                    xTaskCreate(blinkLoop, TASK_NAME, configMINIMAL_STACK_SIZE + STACK_SIZE, &params, 4, &blinkTask);
                    break;
                
                case CHARGING:
                    params = {1000, INFINITE, true};
                    xTaskCreate(blinkLoop, TASK_NAME, configMINIMAL_STACK_SIZE + STACK_SIZE, &params, 4, &blinkTask);
           } 
        }

        static void blinkLoop(void *pvParameters) {
            Params* params = (Params*) pvParameters;

            do {
                if(params->times == INFINITE) blinkOnce(params);
                else {
                    for(int i = 0; i < params->times; i++) blinkOnce(params);
                    analogWrite(LED_PIN, 255);
                    vTaskDelay(params->duration * (params->times - 1));
                }

            } while(true);
        }

        static void blinkOnce(Params* params) {
            analogWrite(LED_PIN, 255);
            
            if (params->fadeIn) {
                vTaskDelay(params->duration / 2);
                for (int i = 0; i < params->duration * 1.5; i++) {
                    int value = 255 - round(255.0 / params->duration * i);
                    analogWrite(LED_PIN, value);
                    vTaskDelay(1);
                }
            
            } else {
                Serial.println(params->duration);
                vTaskDelay(params->duration);
                analogWrite(LED_PIN, 0);
                vTaskDelay(params->duration);
            }   
        }

        void endProcess() {
            if(blinkTask != NULL) vTaskDelete(blinkTask);
            blinkTask = NULL;
        }

        void off() {
            endProcess();
            analogWrite(this->pin, 255);
        }

        void on() {
            endProcess();
            analogWrite(this->pin, 0);
        }

        State getCurrentState() {
            return currentState;
        }
        
    private:
        uint8_t pin;
        TaskHandle_t blinkTask;
        Params params;
        State currentState;
};

LED led(LED_PIN);