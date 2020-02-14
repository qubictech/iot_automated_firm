package com.tarms.dev.iot_home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.pusher.pushnotifications.PushNotificationReceivedListener
import com.pusher.pushnotifications.PushNotifications
import com.tarms.dev.iot_home.data.*
import com.tarms.dev.iot_home.databinding.ActivityMainBinding
import java.util.*
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    companion object {
        const val INSTANCE_ID = "c1601c60-0369-4ef2-a111-f0b58ab33841"
        const val SECRET_KEY = "2C4063A7DFCF2A53F70F92D0D0116F9843E0283D21215D4D37F52622B33B72E9"
        const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        subscribeToPusher()

        val firm = Firm(
            Temperature(Date().time, 29.0F, 80.4F),
            Light("bulb1", true),
            Fans("fan1", false),
            Motor("motor1", false),
            Pump("pump1", false)
        )

        val ref = FirebaseDatabase.getInstance().reference
            .child("user/mazharul_sabbir/")
            .child(Date().time.toString())

//        ref.setValue(
//            firm
//        )

        binding.firm = firm

        findViewById<CardView>(R.id.light).setOnClickListener {
            firm.light.l_status = !firm.light.l_status
            binding.invalidateAll()
        }

        findViewById<CardView>(R.id.pump).setOnClickListener {
            firm.pump.p_status = !firm.pump.p_status
            binding.invalidateAll()
        }

    }

    private fun subscribeToPusher() {
        PushNotifications.start(applicationContext, INSTANCE_ID)
        PushNotifications.addDeviceInterest("hello")

        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(
            this,
            object : PushNotificationReceivedListener {
                override fun onMessageReceived(remoteMessage: RemoteMessage) {
                    sendNotification(
                        remoteMessage.notification?.title.toString(),
                        remoteMessage.notification?.body.toString()
                    )
                }

            })
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageTitle: String, messageBody: String) {
        Logger.getLogger(TAG)
            .warning("Notification Received. Title: $messageTitle , Body: $messageBody")

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Firm Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}
