package com.tarms.dev.iot_home.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class Firm(
    @Exclude var temp: Temperature,
    @Exclude var light: Light,
    @Exclude var fans: Fans,
    @Exclude var motor: Motor,
    @Exclude var pump: Pump
) {
    constructor() : this(
        Temperature(Date().time, 29.0F, 80.4F),
        Light("", true),
        Fans("", false),
        Motor("", false),
        Pump("", false)
    )
}