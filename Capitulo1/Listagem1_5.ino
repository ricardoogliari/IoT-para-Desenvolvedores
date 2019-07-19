const int port = 11;

void setup() {
  pinMode(11, OUTPUT);
}

void loop() {
  for (int i = 0; i < 255; i++) {
    analogWrite(port, i);
    delay(100);
  }

  for (int i = 255; i >= 0; i--) {
    analogWrite(port, i);
    delay(100);
  }
}