#include <Arduino.h>

class Button {
    
    public:

        Button(uint8_t pin) {
            this->pin = pin;
            pinMode(pin, INPUT_PULLUP);
        }

        void check() {
            int value = digitalRead(pin);
            if(value == 0) action();
        }

        void setAction(void (*action)()) {
            this->action = action;
        }

    private:
        uint8_t pin;
        void (*action)();
};