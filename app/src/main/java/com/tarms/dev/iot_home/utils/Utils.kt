package com.tarms.dev.iot_home.utils

import java.text.DecimalFormat

interface Utils {
    companion object {
        fun firmRef(uid: String): String {
            return "user/$uid/firm_data/"
        }

        fun userRef(uid: String): String {
            return "user/$uid/user_data/"
        }

        val numberFormat = DecimalFormat("##.##")
    }
}