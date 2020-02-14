import pyrebase as py

from pusher_push_notifications import PushNotifications

firebaseConfig = {
    'apiKey': "AIzaSyCsPL3qYNUZswDhVl42tGIM8RZDnZ8kvxc",
    'authDomain': "iot-abs.firebaseapp.com",
    'databaseURL': "https://iot-abs.firebaseio.com",
    'projectId': "iot-abs",
    'storageBucket': "iot-abs.appspot.com",
    'messagingSenderId': "292141374117",
    'appId': "1:292141374117:web:a3bb8eafa26a2884da4b4c",
    'measurementId': "G-K2R9CTMY8M"
}

firebase = py.initialize_app(firebaseConfig)

db = firebase.database()

beams_client = PushNotifications(
    instance_id='c1601c60-0369-4ef2-a111-f0b58ab33841',
    secret_key='2C4063A7DFCF2A53F70F92D0D0116F9843E0283D21215D4D37F52622B33B72E9',
)

def stream_handler(message):
    if(message['data'] is 1):
      print(message)
      response = beams_client.publish_to_interests(
            interests=['hello'],
            publish_body={
                'apns': {
                    'aps': {
                        'alert': 'Hello!',
                    },
                },
                'fcm': {
                    'notification': {
                        'title': 'Broiler Firm',
                        'body': 'Congratulations! You have done it. Keep it up.',
                    },
                },
            },
        )

      print(response['publishId'])

my_stream = db.child("message").stream(stream_handler, None)