#pragma once
#include <Arduino.h>
#include <ArduinoJson.h>
#include "Sensor.h"
#include "battery.h"
#include "constants.h"
#include "memory.h"

#define SCAN_INTERVAL 1000
#define CONFIG_CHANGED 1
#define EEPROM_ADDRESS 0x50
const int SENSOR_PINS[] = {27, 33, 26, 14, 32, 25};

HX711 scale;

class SensorManager {

    public:

        void initWeightSensors(Config config) {
            scale.begin(SCALE_DT, SCALE_SCK);
            scale.set_offset(config.weight_offset);
            scale.set_scale(WEIGHT_SCALE);
        }

        bool scan() {
            disableAll();
            Wire.begin(SDA_PIN, SCL_PIN);

            bool changed = false;
            String output = "";
            for(int i = 0; i < sizeof(SENSOR_PINS) / sizeof(int); i++) {
                digitalWrite(SENSOR_PINS[i], LOW);
                Sensor* previousSensor = sensors[i];
                Wire.beginTransmission(EEPROM_ADDRESS);

                if(!Wire.endTransmission()) {
                    sensors[i] = new Sensor(i, SENSOR_PINS[i]);
                    char text[100];
                    sprintf(text, "Sensor found on port %d, Device type: %d (%s)\n", i, sensors[i]->getType(), sensors[i]->getTypeName());
                    output += text;
                    
                } else sensors[i] = nullptr;

                if((previousSensor != nullptr ? previousSensor->getId() : 0) != (sensors[i] != nullptr ? sensors[i]->getId() : 0)) changed = true;
            
                digitalWrite(SENSOR_PINS[i], HIGH);
            }
           
            Wire.end();
            lastScan = millis();

            if(changed) Serial.println(output);

            return changed;
        }

        bool burnSensorId(unsigned int port, int id) {
            disableAll();
            Sensor* sensor = sensors[port];
            if(sensor == nullptr) return false;
            
            sensor->setId(id);
            sensor->saveData();

            return true;
        }

        void burn(unsigned int port, Data data) {
            disableAll();
            SensorStorage storage;
            storage.Connect(SENSOR_PINS[port]);
            storage.Write(STRUCT_ADDR, (uint8_t*) &data, sizeof(data));
        }

        void disableAll() {
            for(int pin : SENSOR_PINS) {
                pinMode(pin, OUTPUT);
                digitalWrite(pin, HIGH);
            }
        }

        String buildJSON(bool disableValueReadings=false) {
            DynamicJsonDocument json(1024);
            JsonArray array = json.createNestedArray("sensors");

            for(Sensor* sensor : sensors) {
                if(sensor != nullptr) {
                    JsonObject object = array.createNestedObject();
                    if(!disableValueReadings) object["value"] = sensor->readValue();
                    object["port"] = sensor->getPort(); 
                    object["type"] = sensor->getType();
                    object["id"] = sensor->getId();
                }
            }


            if(!disableValueReadings) {
                json["battery"] = battery.getPercentage();
                json["weight"] = getWeight() / 1000.0;
            }
            json["beehive"] = BEEHIVE_ID;   
            json["status"] = "ok";

            String output;
            serializeJson(json, output); 

            return output;
        }

        Sensor* getSensor(unsigned int port) {
            if(sensors[port] == nullptr) Serial.println("Sensor not found on port " + String(port));
            return sensors[port];
        }

        bool intervalScan() {
            return millis() - lastScan >= SCAN_INTERVAL ? scan() : false;
        }

        int getWeight() {
            float value = scale.get_units();
            return value > 0 ? value : 0;
        }

        long computeWeightOffset(float weight) {
            scale.set_scale(1);
            scale.set_offset(0);
            long target_value = weight * 1000 * WEIGHT_SCALE;
            long result_value = scale.get_units() - target_value;
            scale.set_scale(WEIGHT_SCALE);
            scale.set_offset(result_value);
            return result_value;
        }

    private:
        Sensor* sensors[sizeof(SENSOR_PINS) / sizeof(int)];
        Battery battery = Battery(25);
        long lastScan = 0;

};