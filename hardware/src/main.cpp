#include <Arduino.h>
#include <Wire.h>
#include <HX711.h>
#include <ArduinoJson.h>
#include <Servo.h>

#include "constants.h"
#include "LED.h"
#include "actions.h"
#include "Button.h"
#include "memory.h"
#include "power.h"
#include "enums.h"
#include "sensors/SensorManager.h" 
#include "connection/network.h"
#include "connection/sockets.h"
#include "connection/sim.h"


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

Button button(12);
SIM sim(1);

Config config = {};
long wakeUpTime;
bool wakeUp = false;

//HX711 scale;

void handleActions();

HardwareSerial gsmSerial(1);

 
void setup()
{
  Serial.begin(9600);
  Serial.println();

  if(Power::getWakeUpCause() == COLD_BOOT || Power::getWakeUpCause() == USER_EVENT) wakeUp = true;
 
  led.indicate(POWER);
  handleActions();

  load(&config);

  wakeUpTime = 1701187047;

  updateStatus();

  if(wakeUp && config.paired) {
    socketConnect();
  }

  onSocketActionReceived([](JsonObject action) {
    ActionType actionType = parseActionType(action["type"]);
    String type = action["type"];
    Serial.println(type);

    if(action["executionTime"] == 0) {
      actionManager.execAndThen(actionType, action["id"], action["params"], [](long actionId, String status){
        if(!socketConnectionAvailable()) socketConnect();
        Param params[] = {{"id", String(actionId)}, {"status", status}};
        sendActionToServer(ACTION_FINISHED, params);
      });
      
    }
    else actionManager.schedule(actionType, action["id"], action["executionTime"], action["params"]);
  });
  

  /*factoryReset();
  ScheduledAction updateStatus = {0, UPDATE_STATUS, "{}", wakeUpTime};
  saveAction(updateStatus);*/

/*  long minTimestamp = 0;

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
      
     // deleteAction(action);
      free(scheduledActions[i]);

    } else if(minTimestamp == 0 || action->executionTime < minTimestamp) minTimestamp = action->executionTime;
  }

  free(scheduledActions);

  //updateStatus();
 // 

  //networkManager.POST("/updateActionsStatuses", actionManager.getExecutedActions());

/*

  led.indicate(REQUEST_SUCCESS);



  delay(2000);

  led.off();




  networkManager.turn_wifi_off();*/
  
  //sim.connect();
 // Serial.println(sim.test());
  //sim.check();
  gsmSerial.begin(9600);
//  saveNextTime(minTimestamp);
 // if(!wakeUp) Power::sleep(wakeUpTime + millis() / 1000 - minTimestamp);

 //pinMode(SCL_PIN, OUTPUT);
 //digitalWrite(SCL_PIN, LOW);
 //pinMode(33, INPUT);

  button.setAction(config.paired ? updateStatus : pair);
  button.setLongPressAction([]() {
    if(Power::getState() == BATTERY_POWER) {
      Param actionParams[] = {{"newState", "IDLE"}};
      sendActionToServer(UPDATE_DEVICE_STATE, actionParams);
      Power::sleep(config.interval * 60);

    } else led.indicate(ERROR);
  });

  if(!wakeUp) Power::sleep(config.interval * 60);
}

void connect() {
  led.indicate(CONNECTING);
  switch (config.connectionMode) {
    case GSM:
        break;

    case WIFI:
      networkManager.connect(config.wifi_ssid, config.wifi_password);
      //networkManager.initHTTPS();
      networkManager.setContentType("application/json");
      networkManager.setDefaultHostname(SERVER_URL);
      
    case OTHER_BEEHIVE:
      break;
  }
}

void getActions() {
    networkManager.GET("/getActionsForBeehive?token=" + String(BEEHIVE_ID));
    DynamicJsonDocument response = networkManager.getResponseJSON();
    executeActions(response["actions"]);
    Serial.println(networkManager.getRequestResult());
}

void updateStatus() {
  networkManager.turn_wifi_off();
  sensorManager.initWeightSensors(config);
  sensorManager.scan();

  String json = sensorManager.buildJSON();
  Serial.println(json);

  connect();
  Serial.println("Connected");

  if (config.paired) {
    networkManager.POST("/updateStatus", json);
    if (!networkManager.isRequestSuccessful()) getActions();
    else {
      DynamicJsonDocument response = networkManager.getResponseJSON();
      executeActions(response["actions"]);
      Serial.println(networkManager.getRequestResult());
    }

  } else getActions();
}

void executeActions(JsonArray actions) {
  for(int i = 0; i < actions.size(); i++) {
    JsonObject action = actions[i];
    ActionType actionType = parseActionType(action["type"]);
    Serial.println(actionType);

    if(actionType == UPDATE_STATUS) continue;

    if(action["executionTime"] == 0) actionManager.exec(actionType, action["id"], action["params"]);
    else actionManager.schedule(actionType, action["id"], action["executionTime"], action["params"]);
  }

  if(!actionManager.resultQueueIsEmpty()) {
    networkManager.POST("/updateActionsStatuses", actionManager.getExecutedActions());
    actionManager.runPostExecMethods();
  } 
}

void pair() {
  Serial.println("paring...");
  DynamicJsonDocument data(150);
  data["beehive"] = BEEHIVE_ID;
  data["model"] = MODEL_NAME;

  String json;
  serializeJson(data, json);

  networkManager.POST("/requestPair", json);
  Serial.println(networkManager.getRequestResult());
  if (networkManager.getRequestResult().equals("SUCCESS")) {
    led.indicate(PAIRED);
    config.paired = true;
    save(config);
    delay(1500);
    esp_restart();
  }
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
    socketDisconnect();
    networkManager.turn_wifi_off();
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
    if (!wakeUp) {
      wakeUp = true;
      connect();
      return "DONE";
    } else return "DONE";
    
  }, [](String result) { 
    //socketConnect(); 
    });

  actionManager.addAction(HIBERNATE, [](JsonObject params) -> String { 
    if(wakeUp) {
      Param actionParams[] = {{"newState", "IDLE"}};
      sendActionToServer(UPDATE_DEVICE_STATE, actionParams);
    }
    return "DONE";

  }, [](String result) {
    Power::sleep(config.interval * 60);
  });

  actionManager.addAction(FACTORY_RESET, [](JsonObject params) -> String {
    factoryReset();
    load(&config);
    return "DONE";
  });
  actionManager.addAction(CALIBRATE_WEIGHT_SENSORS, [](JsonObject params) -> String {
    config.weight_offset = sensorManager.computeWeightOffset(params["weight"]);
    save(config);
    return "DONE";
  });
   actionManager.addAction(UNPAIR, [](JsonObject params) -> String {
    config.paired = false;
    save(config);
    return "DONE";

  }, [](String result) {
    delay(500);
    esp_restart();
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

void checkCharging() {
  if(!socket_connecting && millis() - Power::lastCheckTime > CHARGE_CHECK_INTERVAL) {
    if (Power::getState() == CHARGING_IN_PROGRESS) led.indicate(CHARGING);
    else led.indicate(POWER);
  }
}

void loop()
{
  //delay(500);
 // Serial.println(sensorManager.getWeight());
  updateSocket();
  if(NetworkManager::connectionAvailable() && sensorManager.intervalScan() == CONFIG_CHANGED) {
    Param params[] = {{"config", sensorManager.buildJSON(true)}};
    sendActionToServer(UPDATE_DEVICE_CONFIG, params);
  }

  checkCharging();
  /*while (Serial.available())
  {
            gsmSerial.write(Serial.read()); // Forward what Serial received to Software Serial Port
        }
        while (gsmSerial.available())
        {
            Serial.write(gsmSerial.read()); // Forward what Software Serial received to Serial Port
        }*/
}

 
