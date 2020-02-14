package com.tarms.dev.iot_home.data

data class Firm(
    val temp: Temperature,
    val light: Light,
    val fans: Fans,
    val motor: Motor,
    val pump: Pump
)