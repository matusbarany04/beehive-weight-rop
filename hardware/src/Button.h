#include <Arduino.h>

#define PRESS_THRESHOLD 2000
#define TASK_STACK_SIZE 2048
class Button {
    
    public:

        Button(uint8_t pin) {
            this->pin = pin;
            pinMode(pin, INPUT);
        }

        void setAction(void (*action)()) {
            this->action = action;
            xTaskCreate(check, "button", configMINIMAL_STACK_SIZE + TASK_STACK_SIZE, this, tskIDLE_PRIORITY, NULL);
        }

        void setLongPressAction(void (*action)()) {
            this->longPressAction = action;
        }

    private:
        uint8_t pin;
        void (*action)();
        void (*longPressAction)();

        static void check(void* button) {
            Button* btn = (Button*) button;
            long pressTime = 0;

            while(true) {
                int value = digitalRead(btn->pin);

                if(value == 1) pressTime = millis();
                else if(pressTime != 0) {
                    if(millis() - pressTime < PRESS_THRESHOLD) {
                        if(btn->action != NULL) btn->action();
                    } else {
                        if(btn->longPressAction != NULL) btn->longPressAction();
                    }
                    pressTime = 0;
                }
                while(digitalRead(btn->pin) == 1); 
                vTaskDelay(5 / portTICK_RATE_MS);
            }
        }
};