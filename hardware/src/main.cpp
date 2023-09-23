//
//    FILE: HX_kitchen_scale.ino
//  AUTHOR: Rob Tillaart
// PURPOSE: HX711 demo
//     URL: https://github.com/RobTillaart/HX711

#include <constants.h>
#include "HX711.h"
#include <Button.h>
#include <network.h>
#include <ArduinoJson.h>

HX711 scale;

//uint8_t dataPin = 6;
//uint8_t clockPin = 7;
uint8_t dataPin  = 21;//for esp32
uint8_t clockPin = 22;//for esp32

Button button(23);
NetworkManager networkManager;

void setup()
{
  Serial.begin(9600);
  Serial.println(__FILE__);
  Serial.print("LIBRARY VERSION: ");
  Serial.println(HX711_LIB_VERSION);
  Serial.println();

  pinMode(0, INPUT);

  Serial.println(analogRead(0));
  networkManager.connectDefault();
  networkManager.setContentType("application/json");

  button.setAction([]() {
    DynamicJsonDocument data(150);
    data["beehive"] = BEEHIVE_ID;
    data["model"] = MODEL_NAME;
    
    String json;
    serializeJson(data, json);

    networkManager.POST(String(SERVER_URL) + "/requestPair", json);
    Serial.println(networkManager.getRequestResult());
  });

/*
  scale.begin(dataPin, clockPin);

  Serial.print("UNITS: ");
  Serial.println(scale.get_units(10));

  Serial.println("\nEmpty the scale, press a key to continue");
  while(!Serial.available());
  while(Serial.available()) Serial.read();

  scale.tare();
  Serial.print("UNITS: ");
  Serial.println(scale.get_units(10));


  Serial.println("\nPut 1000 gram in the scale, press a key to continue");
  while(!Serial.available());
  while(Serial.available()) Serial.read();

  scale.calibrate_scale(1000, 5);
  Serial.print("UNITS: ");
  Serial.println(scale.get_units(10));

  Serial.println("\nScale is calibrated, press a key to continue");
  // Serial.println(scale.get_scale());
  // Serial.println(scale.get_offset());
  while(!Serial.available());
  while(Serial.available()) Serial.read();*/
}


void loop()
{
 /*Serial.print("UNITS: ");
  Serial.println(scale.get_units(10));*/
  delay(10);
  //Serial.println(analogRead(0));
  button.check();
}


// -- END OF FILE --

