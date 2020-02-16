#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <ESP8266HTTPClient.h>

// Set these to run example.
#define FIREBASE_HOST "iot-abs.firebaseio.com"
#define FIREBASE_AUTH "sKrgUqedBmRkSqiwCDT6CQ0fYcw2dU50n7gkiCYB"

#define WIFI_SSID "Mazharul Sabbir"
#define WIFI_PASSWORD "beniasohokola632294"

#define BASE_URL "https://fcm.googleapis.com/fcm/send"

int temp_output_pin = A0;

void setup() {
  Serial.begin(9600);
  Serial.setDebugOutput(true);

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

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  Serial.println("Starting.....");

  delay(1000);

  pinMode(D7, OUTPUT);     
}

void loop() {
//  fcmNotification("Broiler Firm","Notification is received!","fcmIOT");
  ledBulbStatus();
  temperature();
}

void ledBulbStatus(){
  if(Firebase.getBool("user/mazharul_sabbir/firm_data/1581694698821/light/l_status")){
    digitalWrite(D7, HIGH);
    Serial.println("LED D7 On");
  }else{
    digitalWrite(D7, LOW);
    Serial.println("LED D7 OFF");
  }

  delay(100);
}

void temperature(){
    int analogValue = analogRead(temp_output_pin);
    float millivolts = (analogValue/1024.0) * 3300; //3300 is the voltage provided by NodeMCU
    float celsius = millivolts/10;
    Serial.print("in DegreeC=   ");
    Serial.println(celsius);

    // set value
    Firebase.setFloat("user/mazharul_sabbir/firm_data/1581694698821/temp/c_temp", celsius);
    // handle error
    if (Firebase.failed()) {
      Serial.print("setting /celsius failed:");
      Serial.println(Firebase.error());
      return;
    }

//---------- Here is the calculation for Fahrenheit ----------//

    float fahrenheit = ((celsius * 9)/5 + 32);
    Serial.print(" in Farenheit=   ");
    Serial.println(fahrenheit);

    // set value
    Firebase.setFloat("user/mazharul_sabbir/firm_data/1581694698821/temp/f_temp",fahrenheit);
    // handle error
    if (Firebase.failed()) {
      Serial.print("setting /temp failed:");
      Serial.println(Firebase.error());
      return;
    }
    delay(300);
}

void fcmNotification(String title, String body,String topics) {
  WiFiClient cli;
  HTTPClient mClient;  
  
  String data = "{";
  data = data + "\"to\": \"/topics/" + topics + "\",";
  data = data + "\"notification\": {";
  data = data + "\"body\": \"" + body + "\",";
  data = data + "\"title\" : \"" + title + "\" ";
  data = data + "} }";

  mClient.begin(BASE_URL,"");
  mClient.addHeader("Authorization", "key=AAAARAT7mqU:APA91bHVd239UHrbFpotFlDE0GLRC_Bv79yVPtUGvWaXUyrhrscv8jDSP6k4ABgJJWAmrR2Vwc1jHVmVGtJW0XNVmsVbHXWUUvIG84Qj-_XJsGgiM9JwoA_byXs1NwNDwCuCrqoTR77b");
  mClient.addHeader("Content-Type", "application/json");
  
  int code = mClient.POST(data);
  mClient.writeToStream(&Serial);

 if(code>0){
    Serial.print("Response Code: "+code);
    Serial.println(mClient.getString());
    
    mClient.end();
  }else{
    Serial.print("Error: ");
    Serial.println(mClient.errorToString(code).c_str());
    mClient.end();
    return;
  }
}
