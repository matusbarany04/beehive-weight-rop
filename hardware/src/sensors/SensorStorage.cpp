#include "Arduino.h"
#include "SensorStorage.h"
#include "constants.h"


SensorStorage::SensorStorage(int devAddress = 0x50) {
  deviceAddress = devAddress;
}

void SensorStorage::Connect(int pin) {
  pinMode(pin, OUTPUT);
  digitalWrite(pin, LOW);
  Wire.begin(SDA_PIN, SCL_PIN);
}


// podprogram pro zapsání jednoho znaku
void SensorStorage::Write(unsigned int address, uint8_t data) {
   if(this->Read(address) != data){
    Wire.beginTransmission(deviceAddress);
    Wire.write((int)(address));
    Wire.write(data);
    Wire.endTransmission();
    delay(5);
  }
}

// podprogram pro zapsání zprávy o velikosti max. 30 znaků
void SensorStorage::Write(unsigned int address, uint8_t* data, uint8_t length) {
  for (uint8_t i = 0; i < length; i++) {
    Write(address + i, data[i]);
  }
}

// podprogram pro přečtení jednoho znaku
uint8_t SensorStorage::Read(unsigned int address) {
  uint8_t rdata = 0xFF;
  Wire.beginTransmission(deviceAddress);
  Wire.write((int)(address));
  Wire.endTransmission();
  Wire.requestFrom(deviceAddress, 1);
  if (Wire.available()) rdata = Wire.read();
  delay(5);
  return rdata;
}

// podprogram pro přečtení zprávy o velikosti max. 30 znaků
void SensorStorage::Read(unsigned int address, uint8_t* buffer, int length) {
  Wire.beginTransmission(deviceAddress);
  Wire.write((int)(address));
  Wire.endTransmission();
  Wire.requestFrom(deviceAddress, length);
  int c = 0;
  for (c = 0; c < length; c++)
    if (Wire.available()) buffer[c] = Wire.read();
  delay(5);
}

SensorStorage::operator uint8_t() const {
  return this->Read(this->indexToWrite);
}

SensorStorage& SensorStorage::operator[](int index) {
  this->indexToWrite = index;
  return *this;
}
void SensorStorage::operator=(uint8_t data) {
  this->Write(this->indexToWrite, data);
}