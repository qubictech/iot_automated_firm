#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#include "DHT.h"
#include <Wire.h>

#include "ThingSpeak.h"

// Set these to run firebase.
#define FIREBASE_HOST "iot-abs.firebaseio.com"
#define FIREBASE_AUTH "m4dS7dKquNI4s9zrE7B36eUnxizaKA6Se8OYBG95"
#define FIREBASE_USER_REFERENCE""

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

void setup() {
  Serial.begin(115200);
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
}

void getFirebaseData() {
  Firebase.getString(FIREBASE_USER_REFERENCE);
}

void storeThinkSpeakData() {
  // Read temperature as Celsius (the default)
  float temp = dht.readTemperature();

  // Write value to Field 1 of a ThingSpeak Channel
  int httpCode = ThingSpeak.writeField(CHANNEL_NUMBER, 1, temp, WRITE_API_KEY);

  if (httpCode == 200) {
    Serial.println("Channel write successful.");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode));
  }

  // Wait 20 seconds to update the channel again
  delay(20000);

  // read humidity data
  float humidity = dht.readHumidity();
  // Write value to Field 2 of a ThingSpeak Channel
  int result = ThingSpeak.writeField(CHANNEL_NUMBER, 2, humidity, WRITE_API_KEY);

  if (result == 200) {
    Serial.println("Channel write successful.");
  }
  else {
    Serial.println("Problem writing to channel. HTTP error code " + String(httpCode));
  }

  // Wait 20 seconds to update the channel again
  delay(20000);
}

void loop() {
  storeThinkSpeakData();
}
