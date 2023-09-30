#include <Arduino.h>
#include <Wire.h>

#include "sensors/SensorManager.h" 
#include "network.h"
#include "constants.h"

int address = 0;
unsigned int value = 0;
unsigned int check_value = 0;

SensorManager sensorManager;
NetworkManager networkManager;
 
void setup(void)
{
  Serial.begin(9600);

  Serial.println();

  //sensorManager.resetSensor(0);


  delay(1000);

  sensorManager.scan();

  networkManager.connectDefault();

  networkManager.POST(SERVER_URL + "/updateStatus", sensorManager.buildJSON());
  Serial.println(networkManager.getRequestResult());
}
 
void loop()
{  
  delay(500);
}
 /*
void writeEEPROM(int deviceaddress, unsigned int eeaddress, byte data ) 
{
  if (maxaddress <= 255) 
   {
    Wire.beginTransmission(deviceaddress);
    Wire.write((int)(eeaddress));
    Wire.write(data);
    Wire.endTransmission();
   }
   
  if(maxaddress >= 511) 
   {
    Wire.beginTransmission(deviceaddress);
    Wire.write((int)(eeaddress >> 8));   // MSB
    Wire.write((int)(eeaddress & 0xFF)); // LSB
    Wire.write(data);
    Wire.endTransmission();
   }
   
  delay(5);
}
 
byte readEEPROM(int deviceaddress, unsigned int eeaddress ) 
{
  byte rdata = 0xFF;
  
  if(maxaddress <= 255) 
   {
    Wire.beginTransmission(deviceaddress);
    Wire.write((int)(eeaddress));
    Wire.endTransmission();
   }
  
  if(maxaddress >= 511) 
   {
    Wire.beginTransmission(deviceaddress);
    Wire.write((int)(eeaddress >> 8));   // MSB
    Wire.write((int)(eeaddress & 0xFF)); // LSB
    Wire.endTransmission();
   }
  
   Wire.requestFrom(deviceaddress,1);
 
   if (Wire.available()) rdata = Wire.read();
 
   return rdata;
}*/