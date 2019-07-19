#include <SoftwareSerial.h>
SoftwareSerial DebugSerial(2, 3); // RX, TX

#define BLYNK_PRINT DebugSerial
#include <BlynkSimpleStream.h>

// Você deve pegar o valor de Auth Token no Blynk App
// Vá para as confi gurações do projeto
char auth[] = "w34LdJv5PlfX_2yv-du7A5hLvbTxZbZB";

void setup() {
  // console de depuração
  DebugSerial.begin(9600);
  
  // Blynk vai trabalhar através da Serial
  Serial.begin(9600);
  Blynk.begin(auth, Serial);
}

void loop() {
  Blynk.run();
}
