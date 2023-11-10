#include <WiFi.h>
#include <HTTPClient.h>
#include "constants.h"
#include "LED.h"


class NetworkManager {

    public:

        void connectDefault() {
            connect(WIFI_SSID, WIFI_PASSWORD);
        }

        void connect(String ssid, String password) {
            WiFi.mode(WIFI_STA);
            WiFi.begin(ssid, password);
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

        void POST(String url, String data) {
            http.begin(client, url.c_str());
            int httpResponseCode = http.POST(data);  
            result = http.getString();
            http.end();
        }

        void GET(String url) {
            HTTPClient http;
            http.begin(client, url.c_str());
            int httpResponseCode = http.GET();  
            result = http.getString();
            http.end();
        }


        String getRequestResult() {
            return result;
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
        int httpResponseCode;
};