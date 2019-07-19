#include <DHT.h>;
#define DHTPIN A0
#define DHTTYPE DHT11

DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(9600);
  dht.begin();
}

void loop() {
  float h = dht.readHumidity();
  float t = dht.readTemperature();

  if (isnan(t) || isnan(h)) {
    Serial.println("Falha ao ler do DHT");
  }
  else {
    Serial.print("Umidade: ");
    Serial.print(h);
    Serial.print("%. ");
    Serial.print("Temperatura: ");
    Serial.print(t);
    Serial.println("*C");
  }
}
