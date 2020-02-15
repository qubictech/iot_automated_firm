package com.tarms.dev.iot_home.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Motor(var m_name: String? = "", var m_status: Boolean? = false)