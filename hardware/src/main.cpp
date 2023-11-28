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
#include "memory.h"
#include "power.h"
#include "enums.h"

#define WEIGHT_SCALE -34850

void pair();
void connect();
void updateStatus();
void executeActions(JsonArray actions);
String changeConfig(JsonObject params);

int address = 0;
unsigned int value = 0;
unsigned int check_value = 0;


SensorManager sensorManager;
NetworkManager networkManager;
ActionManager actionManager;

LED led(19);
Button button(21);

Config config = {};
long wakeUpTime;
bool wakeUp = false;
bool isConnected = false;

//HX711 scale;

void handleActions();
 
void setup()
{
  Serial.begin(9600);
  led.indicate(OK);
  handleActions();

  load(&config);

  wakeUpTime = 1701187047;

 // factoryReset();
  /*ScheduledAction updateStatus = {0, UPDATE_STATUS, "{}", wakeUpTime};
  saveAction(updateStatus);*/

  long minTimestamp = 0;

  uint8_t actionCount;
  Serial.println("reading actions");
  ScheduledAction** scheduledActions = loadScheduledActions(&actionCount);

  for (int i = 0; i < actionCount; i++) {
    ScheduledAction* action = scheduledActions[i];

    if(action->executionTime <= wakeUpTime) {
      DynamicJsonDocument params(128);
      deserializeJson(params, action->params);
      Serial.println(action->type);
      actionManager.exec(action->type, action->id, params.as<JsonObject>());
      
      deleteAction(action);
      free(scheduledActions[i]);

    } else if(minTimestamp == 0 || action->executionTime < minTimestamp) minTimestamp = action->executionTime;
  }

  free(scheduledActions);

  //updateStatus();
 // 

  //networkManager.POST("/updateActionsStatuses", actionManager.getExecutedActions());

/*
  scale.begin(22, 23);
  scale.set_scale(WEIGHT_SCALE); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale.tare();*/

  //sensorManager.burn(1, {"", 1000, TEMPERATURE, 0});
  //sensorManager.burn(2, {"", 1000, LIGHT, 0});


  led.indicate(REQUEST_SUCCESS);

 // button.setAction(pair);

  delay(2000);

  led.off();

  networkManager.turn_wifi_off();
  saveNextTime(minTimestamp);
  if(!wakeUp) Power::sleep(wakeUpTime + millis() / 1000 - minTimestamp);
}



void connect() {
 // led.indicate(CONNECTING);
  switch (config.connectionMode) {
    case GSM:
        break;

    case WIFI:
      networkManager.connect(config.wifi_ssid, config.wifi_password);
      networkManager.setContentType("application/json");
      networkManager.setDefaultHostname(SERVER_URL);
      
    case OTHER_BEEHIVE:
      break;
  }
  isConnected = true;
}

void updateStatus() {
  sensorManager.scan();

  String json = sensorManager.buildJSON();
  Serial.println(json);

  if(!isConnected) connect();

  Serial.println("Connected");

  networkManager.POST("/updateStatus", json);
  DynamicJsonDocument response = networkManager.getResponseJSON();
  executeActions(response["actions"]);
  Serial.println(networkManager.getRequestResult());
}


void executeActions(JsonArray actions) {
  for(int i = 0; i < actions.size(); i++) {
    JsonObject action = actions[i];
    ActionType actionType = parseActionType(action["type"]);

    if(action["executionTime"] == 0) actionManager.exec(actionType, action["id"], action["params"]);
    else actionManager.schedule(actionType, action["id"], action["executionTime"], action["params"]);
  }

  if(actions.size() > 0) networkManager.POST("/updateActionsStatuses", actionManager.getExecutedActions());

  ScheduledAction updateStatus = {0, UPDATE_STATUS, "{}", wakeUpTime + millis() / 1000 + config.interval * 60};
  saveAction(updateStatus);
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
  actionManager.addAction(UPDATE_STATUS, [] (JsonObject params) -> String {
    updateStatus();
    return "DONE";
  });
  actionManager.addAction(BURN_SENSOR_ID, [] (JsonObject params) -> String {
    bool success = sensorManager.burnSensorId(params["port"], params["sensorId"]);
    return success ? "DONE" : "DEVICE_NOT_FOUND";
  });
  actionManager.addAction(CHANGE_BEEHIVE_CONFIG, [](JsonObject params) -> String {
    return changeConfig(params);
  });
  actionManager.addAction(WAKE_UP, [](JsonObject params) -> String { 
    wakeUp = true;
    return "DONE";
  });
  actionManager.addAction(FACTORY_RESET, [](JsonObject params) -> String {
    factoryReset();
    load(&config);
    return "DONE";
  });
}

String changeConfig(JsonObject params) {
    String interval = params["interval"];
    if(params.containsKey("interval")) config.interval = params["interval"];
    if(params.containsKey("connectionMode")) config.connectionMode = parseConnectionMode(params["connectionMode"]);
    save(config);
    
    String ssid = params["wifi_ssid"];
    String wifi_passwd = params["wifi_password"];

    if(ssid != "null" && wifi_passwd != "null") {
      led.indicate(CONNECTING);
      String status = testConnection(ssid, wifi_passwd);
      
      if(status.equals("DONE")) {
        memcpy(config.wifi_ssid, ssid.c_str(), sizeof(config.wifi_ssid));
        memcpy(config.wifi_password, wifi_passwd.c_str(), sizeof(config.wifi_password));
        save(config);

      } else connect();

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
 
