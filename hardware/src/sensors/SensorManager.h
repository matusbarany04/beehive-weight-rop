#pragma once
#include <Arduino.h>
#include <ArduinoJson.h>
#include "Sensor.h"
#include "battery.h"
#include "constants.h"

#define EEPROM_ADDRESS 0x50
const int SENSOR_PINS[] = {2, 15, 4};


class SensorManager {

    public:

        void scan() {
            disableAll();
            Wire.begin(SDA_PIN, SCL_PIN);

            for(int i = 0; i < sizeof(SENSOR_PINS) / sizeof(int); i++) {
                digitalWrite(SENSOR_PINS[i], LOW);
                Wire.beginTransmission(EEPROM_ADDRESS);

                if(!Wire.endTransmission()) {
                    sensors[i] = new Sensor(i, SENSOR_PINS[i]);
                    char output[100];
                    sprintf(output, "Sensor found on port %d, Device type: %d (%s)", i, sensors[i]->getType(), sensors[i]->getTypeName());
                    Serial.println(output);
                    
                } else sensors[i] = nullptr;
            
                digitalWrite(SENSOR_PINS[i], HIGH);
            }
           
            Wire.end();
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

        String buildJSON() {
            DynamicJsonDocument json(1024);
            JsonArray array = json.createNestedArray("sensors");

            for(Sensor* sensor : sensors) {
                if(sensor != nullptr) {
                    JsonObject object = array.createNestedObject();
                    object["value"] = sensor->readValue();
                    object["port"] = sensor->getPort(); 
                    object["type"] = sensor->getType();
                    object["id"] = sensor->getId();
                }
            }

            json["weight"] = 45;
            json["beehive"] = BEEHIVE_ID;
            json["battery"] = battery.getPercentage();   
            json["status"] = "ok";

            String output;
            serializeJson(json, output); 

            return output;
        }

        Sensor* getSensor(unsigned int port) {
            if(sensors[port] == nullptr) Serial.println("Sensor not found on port " + String(port));
            return sensors[port];
        }

    private:
        Sensor* sensors[sizeof(SENSOR_PINS) / sizeof(int)];
        Battery battery = Battery(25);

};