#ifndef SensorStorage_h

#define SensorStorage_h 1

#include <Wire.h>
#include "memoryOrganization.h"


class SensorStorage {
public:
  SensorStorage(int devAddress = 0x50);
  void Connect(int pin);
  void Write(unsigned int address, uint8_t data);
  void Write(unsigned int address, uint8_t* data, uint8_t length);
  uint8_t Read(unsigned int address);
  String ReadString(unsigned int address);
  void Read(unsigned int address, uint8_t* buffer, int length);
  SensorStorage& operator[](int index);
  void operator=(uint8_t data);
  void operator=(const char* data);
  operator uint8_t() const;
private:
  int deviceAddress = 0x50;
  short indexToWrite = -1;
};


#endif