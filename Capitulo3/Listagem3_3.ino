void setup() {
  Serial.begin(9600);
  pinMode(13, OUTPUT);
}

void loop() {
  if (Serial.available() > 0) {
    char charReceived = Serial.read();

    if (charReceived == '1'){
      digitalWrite(13, HIGH);
    } else if (charReceived == '0'){
      digitalWrite(13, LOW);
    }
  }
}