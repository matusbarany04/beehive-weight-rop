#include <Arduino.h>
#include <Wire.h>
#include <HX711.h>
#include <ArduinoJson.h>

#include "sensors/SensorManager.h" 
#include "network.h"
#include "constants.h"
#include "actions.h"

#define WEIGHT_SCALE -34850

int address = 0;
unsigned int value = 0;
unsigned int check_value = 0;

SensorManager sensorManager;
NetworkManager networkManager;
ActionManager actionManager;

//HX711 scale;

void handleActions();
 
void setup(void)
{
  Serial.begin(9600);

/*
  scale.begin(22, 23);
  scale.set_scale(WEIGHT_SCALE); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale.tare();*/


  //sensorManager.resetSensor(0);


  delay(1000);

  sensorManager.scan();

  Serial.println("JSON:");
  String json = sensorManager.buildJSON();
  Serial.println(json);

  networkManager.connectDefault();
  networkManager.setContentType("application/json");

  handleActions();

  networkManager.POST(String(SERVER_URL) + "/updateStatus", json);
  DynamicJsonDocument response = networkManager.getResponseJSON();
  JsonArray actions = response["actions"];

  for(int i = 0; i < actions.size(); i++) {
    JsonObject action = actions[i];
    actionManager.exec(action["name"]);
  }

  Serial.println(networkManager.getRequestResult());
}

void handleActions() {
  actionManager.addAction("BURN_SENSOR_ID", [] (float value, unsigned int port) {
    sensorManager.burnSensorId(port, value);
  });
}

 
void loop()
{  
 // Serial.println(scale.get_units(), 1);
  delay(500);
  //Serial.println(sensorManager.getSensor(1)->readValue());
 /*byte error, address;
  int nDevices;
 
  Serial.println("Scanning...");
 
  nDevices = 0;
  for(address = 1; address < 127; address++ )
  {
    // The i2c_scanner uses the return value of
    // the Write.endTransmisstion to see if
    // a device did acknowledge to the address.
    Wire.beginTransmission(address);
    error = Wire.endTransmission();
 
    if (error == 0)
    {
      Serial.print("I2C device found at address 0x");
      if (address<16)
        Serial.print("0");
      Serial.print(address,HEX);
      Serial.println("  !");
 
      nDevices++;
    }
    else if (error==4)
    {
      Serial.print("Unknown error at address 0x");
      if (address<16)
        Serial.print("0");
      Serial.println(address,HEX);
    }    
  }
  if (nDevices == 0)
    Serial.println("No I2C devices found\n");
  else
    Serial.println("done\n");
 
  delay(5000);           // wait 5 seconds for next sca*/
}
 
