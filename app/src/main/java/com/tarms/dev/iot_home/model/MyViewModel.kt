package com.tarms.dev.iot_home.model

import androidx.lifecycle.ViewModel
import com.tarms.dev.iot_home.data.Firm
import com.tarms.dev.iot_home.utils.FirmRepository

class MyViewModel : ViewModel() {

    fun getFirmData() = FirmRepository.getFirmData()

    fun getCurrentData() = FirmRepository.getCurrentData()

    fun updateFirmData(firm: List<Firm>) {
        FirmRepository.updateFirmData(firm)
    }
}