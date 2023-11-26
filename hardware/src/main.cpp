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
    if(action["execution_time"] == 0) actionManager.exec(action["type"], action["id"], action["params"]);
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
  if(status == WL_CONNECTED) return "DONE";
  else if(status == WL_CONNECT_FAILED) return "WIFI_CONNECTION_FAILED";
  else if(status == WL_NO_SSID_AVAIL) return "WIFI_NO_SSID_AVAIL";
}

void handleActions() {
  actionManager.addAction("BURN_SENSOR_ID", [] (JsonObject params) -> String {
    sensorManager.burnSensorId(params["port"], params["sensorId"]);
    return "DONE";
  });
  actionManager.addAction("CHANGE_BEEHIVE_CONFIG", [](JsonObject params) -> String { 

   /* Config config = {params["interval"], params["sim_password"], params["wifi_ssid"], params["wifi_password"]};
    save(config);*/
    if(params["interval"] != "null") config.interval = params["interval"];
    if(params["connectionMode"] != "null") config.connectionMode = params["connectionMode"].as<String>();

    save(config);
    
    String ssid = params["wifi_ssid"];
    String wifi_passwd = params["wifi_password"];

    if(ssid != "null" && wifi_passwd != "null") {
      String status = testConnection(ssid, wifi_passwd);
      if(status != "DONE") {
        config.wifi_ssid = ssid;
        config.wifi_password = wifi_passwd;
        save(config);
      }
      return status;
    }

    return "DONE";
  });
}



 
void loop()
{  
 // Serial.println(scale.get_units(), 1);
  delay(500);
}
 
