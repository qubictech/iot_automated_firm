package com.tarms.dev.iot_home.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tarms.dev.iot_home.data.Firm

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