package com.tarms.dev.iot_home.utils

interface Utils {
    companion object {
        fun ref(uid: String): String {
            return "user/$uid/"
        }
    }
}