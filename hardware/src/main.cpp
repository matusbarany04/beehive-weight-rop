#include <Arduino.h>
#include <Wire.h>
#include <HX711.h>
#include <ArduinoJson.h>

#include "sensors/SensorManager.h" 
#include "network.h"
#include "constants.h"
#include "actions.h"
#include "led.h"
#include "Button.h"
#include "configuration.h"

#define WEIGHT_SCALE -34850

void pair();
String changeConfig(JsonObject params);

int address = 0;
unsigned int value = 0;
unsigned int check_value = 0;


SensorManager sensorManager;
NetworkManager networkManager;
ActionManager actionManager;

LED led(19);
Button button(21);

Config config;

//HX711 scale;

void handleActions();
 
void setup(void)
{
  Serial.begin(9600);
  led.indicate(OK);

  load(config);

  config.wifi_ssid = WIFI_SSID;
  config.wifi_password = WIFI_PASSWORD;

/*
  scale.begin(22, 23);
  scale.set_scale(WEIGHT_SCALE); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale.tare();*/


  //sensorManager.resetSensor(0);
  led.indicate(CONNECTING);

  //sensorManager.burn(1, {"", 1000, TEMPERATURE, 0});
  //sensorManager.burn(2, {"", 1000, LIGHT, 0});

  sensorManager.scan();

  Serial.println("JSON:");
  String json = sensorManager.buildJSON();
  Serial.println(json);

  networkManager.connectDefault();
  networkManager.setContentType("application/json");
  networkManager.setDefaultHostname(SERVER_URL);


  handleActions();

  networkManager.POST("/updateStatus", json);
  DynamicJsonDocument response = networkManager.getResponseJSON();
  JsonArray actions = response["actions"];

  Serial.println(networkManager.getRequestResult());

  for(int i = 0; i < actions.size(); i++) {
    JsonObject action = actions[i];
    String type = action["type"];
    if(action["executionTime"] == 0) actionManager.exec(action["type"], action["id"], action["params"]);
  }

  if(actions.size() > 0) networkManager.POST("/updateActionsStatuses", actionManager.getExecutedActions());
  

  led.indicate(REQUEST_SUCCESS);

 // button.setAction(pair);

  delay(2000);

  led.off();
}

void pair() {
    DynamicJsonDocument data(150);
    data["beehive"] = BEEHIVE_ID;
    data["model"] = MODEL_NAME;

    String json;
    serializeJson(data, json);

    networkManager.POST("/requestPair", json);
    Serial.println(networkManager.getRequestResult());
}

String testConnection(String wifiSSID, String wifiPasswd) {
  wl_status_t status = networkManager.connect(wifiSSID, wifiPasswd);
  
  switch(status) {
    case WL_CONNECTED: return "DONE";
    case WL_CONNECT_FAILED: return "WIFI_CONNECTION_FAILED";
    case WL_NO_SSID_AVAIL: return "WIFI_NO_SSID_AVAIL";
    default: return "UNKOWN_ERROR";
  }
}

void handleActions() {
  actionManager.addAction("BURN_SENSOR_ID", [] (JsonObject params) -> String {
    bool success = sensorManager.burnSensorId(params["port"], params["sensorId"]);
    return success ? "DONE" : "DEVICE_NOT_FOUND";
  });
  actionManager.addAction("CHANGE_BEEHIVE_CONFIG", [](JsonObject params) -> String { 
    return changeConfig(params);
  });
}

String changeConfig(JsonObject params) {
    String interval =  params["interval"];
    Serial.println(interval);
    if(params["interval"] != "null") config.interval = params["interval"];
    if(params["connectionMode"] != "null") config.connectionMode = params["connectionMode"].as<String>();

    save(config);
    
    String ssid = params["wifi_ssid"];
    String wifi_passwd = params["wifi_password"];

    if(ssid != "null" && wifi_passwd != "null") {
      led.indicate(CONNECTING);
      String status = testConnection(ssid, wifi_passwd);
      
      if(status.equals("DONE")) {
        config.wifi_ssid = ssid;
        config.wifi_password = wifi_passwd;
        save(config);

      } else networkManager.connect(config.wifi_ssid, config.wifi_password);

      return status;
    }

   // led.indicate(OK);

    return "DONE";
}



 
void loop()
{  
 // Serial.println(scale.get_units(), 1);
  delay(500);
}
 
