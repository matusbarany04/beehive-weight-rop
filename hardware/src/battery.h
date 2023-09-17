#define DEAD 1700
#define FULL 2430
#define ESP_VOLTAGE 3.3
#define ESP_MAX_IN 4095

class Battery {
    
    public:

        Battery(uint8_t pin) {
            this->pin = pin;
            pinMode(pin, INPUT);
        }

        int getPercentage() {
            int value = analogRead(pin);
            return map(value, DEAD, FULL, 0, 100);
        }

        float getVoltage() {
            int value = analogRead(pin);
            return (value * ESP_VOLTAGE / ESP_MAX_IN + (value < 3400 ? 0.1 : 0)) * 2;
        }

    private:

        uint8_t pin;
};