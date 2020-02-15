package com.tarms.dev.iot_home.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object FirmRepository {

    private val firmData = MutableLiveData<List<Firm>>()

    private val currentFirmData = MutableLiveData<Firm>()

    fun updateFirmData(firm: List<Firm>) {
        firmData.value = firm
        currentFirmData.value = firmData.value?.get(0)
    }

    fun getFirmData(): LiveData<List<Firm>> {
        return firmData
    }

    fun getCurrentData(): LiveData<Firm> {
        return currentFirmData
    }
}