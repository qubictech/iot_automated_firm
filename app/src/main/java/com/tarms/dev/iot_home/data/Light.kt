package com.tarms.dev.iot_home.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Light(var l_name: String = "", var l_status: Boolean? = false)