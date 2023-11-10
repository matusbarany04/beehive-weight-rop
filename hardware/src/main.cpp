#include <Arduino.h>
#include <Wire.h>
#include <HX711.h>

#include "sensors/SensorManager.h" 
#include "network.h"
#include "constants.h"
#include "led.h"
#include "Button.h"

#define WEIGHT_SCALE -34850

void pair();

int address = 0;
unsigned int value = 0;
unsigned int check_value = 0;


SensorManager sensorManager;
NetworkManager networkManager;

LED led(19);
Button button(21);

//HX711 scale;
 
void setup(void)
{
  Serial.begin(9600);
  led.indicate(OK);

/*
  scale.begin(22, 23);
  scale.set_scale(WEIGHT_SCALE); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale.tare();*/


  //sensorManager.resetSensor(0);
  led.indicate(CONNECTING);


  sensorManager.scan();

  Serial.println("JSON:");
  String json = sensorManager.buildJSON();
  Serial.println(json);

  networkManager.connectDefault();
  networkManager.setContentType("application/json");

  networkManager.POST(String(SERVER_URL) + "/updateStatus", json);
  Serial.println(networkManager.getRequestResult());

  led.indicate(REQUEST_SUCCESS);

  button.setAction(pair);

  delay(2000);

  led.off();
}

void pair() {
    DynamicJsonDocument data(150);
    data["beehive"] = BEEHIVE_ID;
    data["model"] = MODEL_NAME;

    String json;
    serializeJson(data, json);

    networkManager.POST(String(SERVER_URL) + "/requestPair", json);
    Serial.println(networkManager.getRequestResult());
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
 
