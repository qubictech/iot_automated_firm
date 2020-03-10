package com.tarms.dev.iot_home.repository

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.tarms.dev.iot_home.data.Firm
import com.tarms.dev.iot_home.utils.Utils

class MyViewModel() : ViewModel() {

    fun getFirmData() = FirmRepository.getFirmData()

    fun getCurrentData() = FirmRepository.getCurrentData()

    fun updateFirmData(firm: List<Firm>) {
        FirmRepository.updateFirmData(firm)
    }

    companion object {
        @SuppressLint("SetTextI18n")
        @BindingAdapter("android:convertTempFloatToString")
        @JvmStatic
        fun convertTempFloatToString(view: TextView, c_temp: Float) {
            view.text = "${Utils.numberFormat.format(c_temp)} °C"
        }

        @SuppressLint("SetTextI18n")
        @BindingAdapter("android:onOffStatus")
        @JvmStatic
        fun onOffStatus(view: TextView, boolean: Boolean) {
            if (boolean) view.text = "On" else view.text = "Off"

        }
    }
}