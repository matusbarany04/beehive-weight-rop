#include <Arduino.h>

class Button {
    
    public:

        Button(uint8_t pin) {
            this->pin = pin;
            pinMode(pin, INPUT);
        }

        void setAction(void (*action)()) {
            this->action = action;
            xTaskCreate(check, "button", configMINIMAL_STACK_SIZE + 1000, this, tskIDLE_PRIORITY, NULL);
        }

    private:
        uint8_t pin;
        void (*action)();

        static void check(void* button) {
            Button* btn = (Button*) button;
            while(true) {
                int value = digitalRead(btn->pin);
                if(value == 1 && btn->action != NULL) {
                    btn->action();
                    while(digitalRead(btn->pin) == 1); 
                }
                vTaskDelay(5 / portTICK_RATE_MS);
            }
        }
};