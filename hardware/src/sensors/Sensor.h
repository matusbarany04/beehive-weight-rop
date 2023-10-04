#pragma once
#include <Arduino.h>
#include "SensorStorage.h"

#define STRUCT_ADDR 0

#define TEMPERATURE 0
#define TEMP_HUMID 1
#define LIGHT 2
#define SOUND 3

struct Data {
  String beehive; 
  int type;
  int id;
};

class Sensor {

    public:

        Sensor(unsigned int port) {
            storage.Connect(port);
            storage.Read(STRUCT_ADDR, (uint8_t*) &this->data, sizeof(Data));
        }

        void setId(int id) {
            this->data.id = id;
        }

        void setBeehive(String beehive) {
            this->data.beehive = beehive;
        }

        int getId() { 
            return this->data.id; 
        }

        unsigned int getPort() { 
            return this->port;
        }

        String getBeehive() { 
            return this->data.beehive; 
        }

        int getType() {
            return this->data.type;
        }

        int readValue() {
            return 0;
        }

        void saveData() {
            storage.Connect(port);
            storage.Write(STRUCT_ADDR, (uint8_t*) &data, sizeof(data));
        }

    private:

        Data data;
        SensorStorage storage;
        unsigned int port;
};