#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include "constants.h"
#include "constants.h"
#include "LED.h"


class NetworkManager {

    public:

        void connectDefault() {
            connect(WIFI_SSID, WIFI_PASSWORD);
        }

        void connect(String ssid, String password) {
            WiFi.mode(WIFI_STA);
            WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
            Serial.print("Connecting");
           
            while (WiFi.status() != WL_CONNECTED) {
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
            http.addHeader("Content-Type", contentType);
        }

        void setDefaultHostname(String hostname) {
            this->hostname = hostname;
        } 

        void POST(String url, String data) {
            if(url.indexOf('.') == -1) url = hostname + url;
            http.begin(client, url.c_str());
            int httpResponseCode = http.POST(data);  
            result = http.getString();
            http.end();
        }

        void GET(String url) {
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
            DynamicJsonDocument doc(256);
            deserializeJson(doc, getRequestResult());
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
        int httpResponseCode;
};