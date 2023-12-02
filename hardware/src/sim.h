#include <Arduino.h>

#define BAUDRATE 9600
#define ORANGE_APN "internet"

#define CMD_TIMEOUT 1000

class SIM
{

public:

    SIM(int serial) : gsmSerial(serial)
    {
       
    }

    void connect() {
        gsmSerial.begin(BAUDRATE);
    }

    void unlock(String pin) {
        Serial.println(exec("AT+CPIN=" + pin));
    }

    bool connectGPRS() {
        return exec("AT+SAPBR=3,1,\"Contype\",\"GPRS\"").equals("OK")
            && exec(String("AT+SAPBR=3,1,APN,") + ORANGE_APN).equals("OK");
    }

    String httpGET(String url) {
        if(!exec("AT+SAPBR=1,1").equals("OK")) return "GPRS OPEN FAILED";
        if(!exec("AT+HTTPINIT").equals("OK")) return "HTTP INIT FAILED";
        if(url.startsWith("https://")) exec("AT+HTTPSSL=1");

        exec("AT+HTTPPARA=CID,1");
        Serial.println(exec(String("AT+HTTPPARA=URL,") + url, 3000));

        Serial.println(exec("AT+HTTPACTION=0", 10000));
        delay(10000);

        exec("AT+HTTPREAD");
        delay(100);
        String response = exec("AT+HTTPREAD", 10000);
        delay(2000);
        exec("AT+SAPBR=0,1");
        exec("AT+HTTPTERM", 5000);
        return response;
    }

    String httpPOST(String url, String data) {
        if(!exec("AT+SAPBR=1,1").equals("OK")) return "GPRS OPEN FAILED";
        if(!exec("AT+HTTPINIT").equals("OK")) return "HTTP INIT FAILED";
        if(url.startsWith("https://")) exec("AT+HTTPSSL=1");

        exec("AT+HTTPPARA=CID,1");
        Serial.println(exec(String("AT+HTTPPARA=URL,") + url, 2000));
        exec("AT+HTTPPARA=CONTENT,application/json");
        exec("AT+HTTPDATA=" + String(data.length()) + ",100000", 3000);
        gsmSerial.println(data);
        delay(6000);

        Serial.println(exec("AT+HTTPACTION=1", 6000));
        exec("AT+HTTPREAD");
        delay(1000);
        String response = exec("AT+HTTPREAD", 10000);
        delay(2000);
        exec("AT+SAPBR=0,1");
        exec("AT+HTTPTERM", 5000);
        return response;
    }

    void check()
    {
        Serial.println(gsmSerial.baudRate());
        Serial.println(gsmSerial.available());
        while (Serial.available())
        {
            gsmSerial.write(Serial.read()); // Forward what Serial received to Software Serial Port
        }
        while (gsmSerial.available())
        {
            Serial.write(gsmSerial.read()); // Forward what Software Serial received to Serial Port
        }
    }

    bool test() {
        Serial.println("testing AT command");
        String result = exec("AT");
        return result.equals("OK");
    }

private:

    HardwareSerial gsmSerial;

    String exec(String command, int timeout = CMD_TIMEOUT) {
        gsmSerial.println(command);
        while(!gsmSerial.available());

        String result;
        int cmdIndex = 0;
        
        READ_SERIAL:
            char letter = (char) gsmSerial.read();
            if(cmdIndex > command.length()) 
                result += letter;

            cmdIndex++;

            for(int i = 0; i < timeout; i++) {
                if(gsmSerial.available()) goto READ_SERIAL;
                if(i > 10 && cmdIndex > command.length() + 5) break;
                delay(1);
            } 

        result.trim();

        if(result.equals("ERROR")) Serial.printf("command \"%s\" failed.\n", command);
        return result;
    }
};