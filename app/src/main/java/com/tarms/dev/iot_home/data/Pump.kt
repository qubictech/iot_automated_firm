package com.tarms.dev.iot_home.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Pump(var p_name: String? = "", var p_status: Boolean? = false)