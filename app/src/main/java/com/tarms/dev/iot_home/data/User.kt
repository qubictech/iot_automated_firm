package com.tarms.dev.iot_home.data

data class User(var name: String, var mobile: String) {
    constructor() : this("", "")
}