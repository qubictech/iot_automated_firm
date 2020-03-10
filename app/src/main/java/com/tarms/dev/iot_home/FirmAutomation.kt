package com.tarms.dev.iot_home

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class FirmAutomation: Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}