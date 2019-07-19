#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <Ticker.h>
#include "DHT.h"

#define FIREBASE_HOST "integracaofirebase-1c3f0.firebaseio.com"
#define FIREBASE_AUTH "KMnmLjkOGlbSWBqIiJdHrYdNcfIG5ntMPjs5k66V"
#define WIFI_SSID "seu_ssid"
#define WIFI_PASSWORD "seu_password"

#define LAMP_PIN D3
#define DHT_PIN D5
#define DHTTYPE DHT11

#define PUBLISH_INTERVAL 1000*10

DHT dht(DHT_PIN, DHTTYPE);
Ticker ticker;
bool publishNewState = true;

void publish(){
    publishNewState = true;
}

void setupPins(){
    pinMode(LAMP_PIN, OUTPUT);
    digitalWrite(LAMP_PIN, LOW);

    dht.begin();
}

void setupWifi(){
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.println("conectando");
    while (WiFi.status() != WL_CONNECTED) {
        Serial.print(".");
        delay(1000);
    }
    Serial.println();
    Serial.print("conectado: ");
    Serial.println(WiFi.localIP());
}

void setupFirebase(){
    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
    Firebase.setBool("lamp", false);
}

void setup() {
    Serial.begin(9600);
    
    setupPins();
    setupWifi();
    setupFirebase();

    ticker.attach_ms(PUBLISH_INTERVAL, publish);
}

void loop() {
    if(publishNewState){
        float humidity = dht.readHumidity();
        float temperature = dht.readTemperature();
        if(!isnan(humidity) && !isnan(temperature)){
            Firebase.pushFloat("temperature", temperature);
            Firebase.pushFloat("humidity", humidity);
            publishNewState = false;
        }
    }
  
    bool lampValue = Firebase.getBool("lamp");
    digitalWrite(LAMP_PIN, lampValue ? HIGH : LOW);

    delay(200);
}

