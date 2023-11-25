#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include "constants.h"
#include "constants.h"
#include "LED.h"

#define JSON_SIZE 1024


class NetworkManager {

    public:

        void connectDefault() {
            connect(WIFI_SSID, WIFI_PASSWORD);
        }

        wl_status_t connect(String ssid, String password) {
            WiFi.mode(WIFI_STA);
            WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
            Serial.print("Connecting");
           
            while (WiFi.status() == WL_DISCONNECTED || WiFi.status() == WL_IDLE_STATUS) {
                delay(500);
                Serial.print(".");
                Serial.println("Status: " + String(WiFi.status()));
            }

            Serial.println("");
            Serial.print("Connected to ");
            Serial.println(WIFI_SSID);
            Serial.print("IP address: ");
            Serial.println(WiFi.localIP());
        }

        void setContentType(String contentType) {
            this->contentType = contentType;
        }

        void setDefaultHostname(String hostname) {
            this->hostname = hostname;
        } 

        void POST(String url, String data) {
            http.addHeader("Content-Type", contentType);
            if(url.indexOf('.') == -1) url = hostname + url;
            http.begin(client, ("http://" + url).c_str());
            int httpResponseCode = http.POST(data);  
            result = http.getString();
            http.end();
        }

        void GET(String url) {
            http.addHeader("Content-Type", contentType);
            if(url.indexOf('.') == -1) url = hostname + url;
            HTTPClient http;
            http.begin(client, url.c_str());
            int httpResponseCode = http.GET();  
            result = http.getString();
            http.end();
        }


        String getRequestResult() {
            return result;
        }

        DynamicJsonDocument getResponseJSON() {
            String response = getRequestResult();
            DynamicJsonDocument doc(JSON_SIZE);
            Serial.println(measureJson(doc));
            Serial.println(sizeof(response));
            deserializeJson(doc, response);
            return doc; 
        }

        bool isRequestSuccessful() {
            return httpResponseCode > 0;
        }

        void turn_wifi_off() {
            WiFi.mode(WIFI_OFF);
        }

    private:

        WiFiClient client;
        HTTPClient http;
        String result;
        String hostname;
        String contentType;
        int httpResponseCode;
};