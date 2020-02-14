#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <FirebaseCloudMessaging.h>

// Set these to run example.
#define FIREBASE_HOST "iot-abs.firebaseio.com"
#define FIREBASE_AUTH "sKrgUqedBmRkSqiwCDT6CQ0fYcw2dU50n7gkiCYB"
//#define WIFI_SSID "Software Section"
//#define WIFI_PASSWORD "diu123456"

#define WIFI_SSID "Mazharul Sabbir"
#define WIFI_PASSWORD "beniasohokola632294"

#define SERVER_KEY "AAAARAT7mqU:APA91bHVd239UHrbFpotFlDE0GLRC_Bv79yVPtUGvWaXUyrhrscv8jDSP6k4ABgJJWAmrR2Vwc1jHVmVGtJW0XNVmsVbHXWUUvIG84Qj-_XJsGgiM9JwoA_byXs1NwNDwCuCrqoTR77b"
#define CLIENT_REGISTRATION_ID "key_from_client_after_registration"

void setup() {
  Serial.begin(115200);

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print("..");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());

  FirebaseCloudMessaging fcm(SERVER_KEY);
  FirebaseCloudMessage message =
    FirebaseCloudMessage::SimpleNotification("Automated Broiler Firm", "Wifi Connected!");
  FirebaseError error = fcm.SendMessageToUser(CLIENT_REGISTRATION_ID, message);
  if (error) {
    Serial.print("Error:");
    Serial.print(error.code());
    Serial.print(" :: ");
    Serial.println(error.message().c_str());
  } else {
    Serial.println("Sent OK!");
  }

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

int n = 0;

void loop() {

  Serial.println(Firebase.getString("init"));
  if (Firebase.failed()) {
    Serial.print("Error: ");
    Serial.println(Firebase.error());
    return;
  }
  delay(1000);

  // set value
  Firebase.setFloat("number", 42.0);
  // handle error
  if (Firebase.failed()) {
    Serial.print("setting /number failed:");
    Serial.println(Firebase.error());
    return;
  }
  delay(1000);
//
//  // update value
//  Firebase.setFloat("number", 43.0);
//  // handle error
//  if (Firebase.failed()) {
//    Serial.print("setting /number failed:");
//    Serial.println(Firebase.error());
//    return;
//  }
//  delay(1000);
//
//  // get value
//  Serial.print("number: ");
//  Serial.println(Firebase.getFloat("number"));
//  delay(1000);
//  //
//  //  // remove value
//  //  Firebase.remove("number");
//  //  delay(1000);
//
//  // set string value
//  Firebase.setString("message", "hello world");
//  // handle error
//  if (Firebase.failed()) {
//    Serial.print("setting /message failed:");
//    Serial.println(Firebase.error());
//    return;
//  }
//  delay(1000);
//
//  // set bool value
//  Firebase.setBool("truth", false);
//  // handle error
//  if (Firebase.failed()) {
//    Serial.print("setting /truth failed:");
//    Serial.println(Firebase.error());
//    return;
//  }
//  delay(1000);
//
//  // append a new value to /logs
//  String name = Firebase.pushInt("logs", n++);
//  // handle error
//  if (Firebase.failed()) {
//    Serial.print("pushing /logs failed:");
//    Serial.println(Firebase.error());
//    return;
//  }
//  Serial.print("pushed: /logs/");
//  Serial.println(name);
//  delay(1000);
}