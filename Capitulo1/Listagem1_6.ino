void setup() {
  Serial.begin(9600);
}

void loop() {
  int luz = analogRead(2);
  Serial.println(luz);
  delay(1000);
}