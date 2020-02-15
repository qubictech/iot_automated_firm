package com.tarms.dev.iot_home.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Fans(var f_name:String? = "",var f_status:Boolean? = false)