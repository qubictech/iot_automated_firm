package com.tarms.dev.iot_home.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.pusher.pushnotifications.PushNotificationReceivedListener
import com.pusher.pushnotifications.PushNotifications
import com.tarms.dev.iot_home.R
import com.tarms.dev.iot_home.databinding.ActivityMainBinding
import com.tarms.dev.iot_home.ui.fragment.DashboardFragment
import com.tarms.dev.iot_home.ui.fragment.NotificationFragment
import com.tarms.dev.iot_home.ui.fragment.SettingsFragment
import java.util.logging.Logger

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {


    companion object {
        const val PUSHER_INSTANCE_ID = "c1601c60-0369-4ef2-a111-f0b58ab33841"
        const val PUSHER_SECRET_KEY =
            "2C4063A7DFCF2A53F70F92D0D0116F9843E0283D21215D4D37F52622B33B72E9"
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        /*---------------PUSH NOTIFICATION USING PUSHER----------------*/
        PushNotifications.start(
            applicationContext,
            PUSHER_INSTANCE_ID
        )
        PushNotifications.addDeviceInterest("hello")

        pushNotification()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, DashboardFragment())
                .commit()
        }

        findViewById<BottomNavigationView>(R.id.bottom_nav).setOnNavigationItemSelectedListener(this)
    }

    private fun pushNotification() {
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dashboard -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, DashboardFragment())
                    .commit()
            }

            R.id.notifications -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, NotificationFragment())
                    .commit()
            }

            R.id.settings -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingsFragment())
                    .commit()
            }

        }

        return true
    }

}
