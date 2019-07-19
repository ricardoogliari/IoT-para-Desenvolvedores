#include <DHT.h>;
#define DHTPIN A0 
#define DHTTYPE DHT11 
 
DHT dht(DHTPIN, DHTTYPE);
 
void setup() 
{
    Serial.begin(9600);
    dht.begin();
}
 
void loop() 
{
    int inByte = Serial.read();
    switch (inByte) {
    case 'T':
        getTemperature();
        break;
    case 'H':
        getHumidity();
        break;
    }
}

void getTemperature(){
    float temp = dht.readTemperature();
    response(temp);
}

void getHumidity(){
    float hum = dht.readHumidity();
    response(hum);
}

void response(float sensor){
    String value = "$";
    value += sensor;
  
    char charBuf[value.length()];
    value.toCharArray(charBuf, sizeof(charBuf));
  
    Serial.println(charBuf);
}


