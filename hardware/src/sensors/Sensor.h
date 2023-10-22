#pragma once
#include <Arduino.h>
#include "SensorStorage.h"
#include "constants.h"

#define STRUCT_ADDR 0

#define TEMPERATURE 0
#define TEMP_HUMID 1
#define LIGHT 2
#define SOUND 3

#define STANDBY 0
#define ACCESS_MEMORY 1
#define READ_VALUE 2

struct Data {
  String beehive; 
  int type;
  int id;
};

class Sensor {

    public:

        Sensor(unsigned int port, int inputPin) {
            this->port = port;
            this->inputPin = inputPin;
            storage.Connect(inputPin);
            storage.Read(STRUCT_ADDR, (uint8_t*) &this->data, sizeof(Data));
        }

        void setBeehive(String beehive) {
            this->data.beehive = beehive;
        }

        int getId() { 
            return this->data.id; 
        }

        void setId(int id) {
            this->data.id = id;
        }

        int getInputPin() { 
            return this->inputPin;
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

        String getTypeName() {
            switch (this->data.type) {
                case TEMPERATURE: return "Temperature";
                case TEMP_HUMID: return "Humidity";
                case LIGHT: return "Light";
                case SOUND: return "Sound";
                default: return "Unknown";
            } 
        }

        int readValue() {
            setMode(READ_VALUE);
            int value = analogRead(inputPin);
            setMode(STANDBY);
            return value;
        }

        void setMode(short mode) {
            switch(mode) {
                case STANDBY:
                    pinMode(inputPin, OUTPUT);
                    digitalWrite(inputPin, HIGH);
                    break;

                case READ_VALUE:
                    pinMode(SCL_PIN, OUTPUT);
                    digitalWrite(SCL_PIN, LOW);

                case ACCESS_MEMORY:
                    pinMode(inputPin, OUTPUT);
                    digitalWrite(inputPin, LOW);
            }
        }

        void saveData() {
            storage.Connect(inputPin);
            storage.Write(STRUCT_ADDR, (uint8_t*) &data, sizeof(data));
        }

    private:

        Data data;
        SensorStorage storage;
        unsigned int port;
        int inputPin;
};