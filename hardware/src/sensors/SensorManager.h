#pragma once
#include <Arduino.h>
#include <ArduinoJson.h>
#include "Sensor.h"
#include "battery.h"
#include "constants.h"

#define PORT_COUNT 8



class SensorManager {

    public:

        void setMode(int mode) {

        }

        void scan() {
            //TODO: detect all connected sensors
            Sensor sensor = Sensor(0);
            sensors[0] = &sensor;
        }

        void resetSensor(unsigned int port) {
            SensorStorage storage;
            storage.Connect(port);
            Data data = {"", TEMPERATURE, 0};

            storage.Write(STRUCT_ADDR, (uint8_t*) &data, sizeof(data));
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


    private:
        Sensor* sensors[PORT_COUNT];
        Battery battery = Battery(25);

};