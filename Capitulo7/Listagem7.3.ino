#include <SoftwareSerial.h>
SoftwareSerial DebugSerial(2, 3); // RX, TX

#define BLYNK_PRINT DebugSerial
#include <BlynkSimpleStream.h>

char auth[] = "w34LdJv5PlfX_2yv-du7A5hLvbTabcde";

#include <math.h>

const int B = 4275; // B = valor do termistor
const int R0 = 100000; // R0 = 100k
const int pinTempSensor = A0; // Grove – Sensor de temperatura conectado na A0

void setup() {
  DebugSerial.begin(9600);
  
  Serial.begin(9600);
  Blynk.begin(auth, Serial);
}

void loop() {
  Blynk.run();
}

BLYNK_READ(V5) // Blynk app tem algo na porta virtual V5
{
  int a = analogRead(pinTempSensor);
  float R = 1023.0/a-1.0;
  R = R0*R;
  float temperature = 1.0/(log(R/R0)/B+1/298.15)-273.15;
  Blynk.virtualWrite(V5, temperature); // enviando para Blynk

  if (temperature > 23) {
    Blynk.email("Ar-condicionado", "A temperatura ultrapassou os 23 graus. Ligue o ar-condicionado!");
  }
}
