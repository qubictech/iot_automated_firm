#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#include "DHT.h"
#include <Wire.h>

#include "ThingSpeak.h"

#include <SoftwareSerial.h>

// Set these to run firebase.
#define FIREBASE_HOST "iot-abs.firebaseio.com"
#define FIREBASE_AUTH "m4dS7dKquNI4s9zrE7B36eUnxizaKA6Se8OYBG95"
#define FIREBASE_USER_REFERENCE "user/AoFjPjVl0DRyKrIwvqPZNBI6HZ32/firm_data/devices/"

// Wifi name and password
#define WIFI_SSID "MERCUSYS"
#define WIFI_PASSWORD "beniasohokola123"

// ThinkSpeak api
#define CHANNEL_NUMBER 1125511
#define WRITE_API_KEY "GLRPZJZ768UMGPA8"
#define READ_API_KEY "H1X7OCPWB21LMY7U"

// Temperature and Humidity sensor
#define DHTPIN 0
#define DHTTYPE DHT11

DHT dht(DHTPIN, DHTTYPE);
WiFiClient  client;

int mLoopCount = 0;

void setup() {
  Serial.begin(115200);
  Serial1.begin(115200);
  delay(100);

  dht.begin();
  ThingSpeak.begin(client);

  Serial.print("Connecting to ");
  Serial.println(WIFI_SSID);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }


  Serial.println("WiFi is connected");

  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  // motor driver setup
  //  setupMotorDriverL293D();

  pinMode(D1, OUTPUT);
  pinMode(D2, OUTPUT);
  pinMode(D5, OUTPUT);
  pinMode(D6, OUTPUT);
  pinMode(D7, OUTPUT);
  digitalWrite(D1, HIGH);
}

void setupMotorDriverL293D() {
  // pinMode(D0, OUTPUT); // enable pin
  pinMode(D1, OUTPUT); // input pin
  pinMode(D2, OUTPUT); // input pin
}

void getFirebaseData() {
  String fanRef = String(FIREBASE_USER_REFERENCE) + String("device1/status");
  String lightRef = String(FIREBASE_USER_REFERENCE) + String("device2/status");
  String motorRef = String(FIREBASE_USER_REFERENCE) + String("device3/status");
  String pumpRef = String(FIREBASE_USER_REFERENCE) + String("device4/status");

  bool fanStatus = Firebase.getBool(fanRef);
  bool lightStatus = Firebase.getBool(lightRef);
  bool motorStatus = Firebase.getBool(motorRef);
  bool pumpStatus = Firebase.getBool(pumpRef);

  Serial.println(lightStatus);

  if (fanStatus == true) {
    digitalWrite(D1, LOW);
    Serial.println("Fan on");

    Serial1.print(1);
  } else {
    digitalWrite(D1, HIGH);
    Serial.println("Fan off");

    Serial1.print(2);
  }

  if (lightStatus == true) {
    digitalWrite(D1, LOW);
    Serial.println("Light on");

    Serial1.print(3);
  } else {
    digitalWrite(D1, HIGH);
    Serial.println("Light off");

    Serial1.print(4);
  }

  if (motorStatus == true) {
    digitalWrite(D1, LOW);
    Serial.println("Motor on");

    Serial1.print(5);
  } else {
    digitalWrite(D1, HIGH);
    Serial.println("Motor off");

    Serial1.print(6);
  }

  if (pumpStatus == true) {
    digitalWrite(D1, LOW);
    Serial.println("Pump on");

    Serial1.print(7);
  } else {
    digitalWrite(D1, HIGH);
    Serial.println("Pump off");

    Serial1.print(8);
  }

  Serial.print("D1 Status");
  Serial.println(digitalRead(D1));
  delay(1000);
}

void storeThinkSpeakData() {

  if (mLoopCount == 2) {
    // Read temperature as Celsius (the default)
    float temp = dht.readTemperature();
    Serial.println("Temperature: ");
    Serial.print(temp);
    Serial.println();

    // Write value to Field 1 of a ThingSpeak Channel
    int httpCode = ThingSpeak.writeField(CHANNEL_NUMBER, 1, temp, WRITE_API_KEY);

    if (httpCode == 200) {
      Serial.println("Channel write successful.");
    }
    else {
      Serial.println("Problem writing to channel. HTTP error code " + String(httpCode));
    }
  }

  if (mLoopCount == 4) {
    // read humidity data
    float humidity = dht.readHumidity();
    Serial.println("Humidity: ");
    Serial.print(humidity);
    Serial.println();

    // Write value to Field 2 of a ThingSpeak Channel
    int result = ThingSpeak.writeField(CHANNEL_NUMBER, 2, humidity, WRITE_API_KEY);

    if (result == 200) {
      Serial.println("Channel write successful.");
    }
    else {
      Serial.println("Problem writing to channel. HTTP error code " + String(result));
    }

    mLoopCount = 0;
  }
}

void loop() {
  storeThinkSpeakData();
  getFirebaseData();
  mLoopCount++;
  digitalWrite(D1, HIGH);
  digitalWrite(D2, HIGH);
  digitalWrite(D5, HIGH);
  digitalWrite(D6, HIGH);
  digitalWrite(D7, HIGH);
  delay(1000);
  digitalWrite(D1, LOW);
  digitalWrite(D2, LOW);
  digitalWrite(D5, LOW);
  digitalWrite(D6, LOW);
  digitalWrite(D7, LOW);
}
