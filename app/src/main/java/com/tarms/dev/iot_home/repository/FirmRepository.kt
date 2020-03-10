package com.tarms.dev.iot_home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.tarms.dev.iot_home.data.Firm
import com.tarms.dev.iot_home.utils.Utils

object FirmRepository {

    private val firmData = MutableLiveData<List<Firm>>()

    private val currentFirmData = MutableLiveData<Firm>()

    fun updateFirmData(firm: List<Firm>) {
        firmData.value = firm
        currentFirmData.value = firmData.value?.get(0)
    }

    fun updateCurrentData(firm: Firm) {
        currentFirmData.value = firm
    }

    fun getFirmData(): LiveData<List<Firm>> {
        return firmData
    }

    fun getCurrentData(): LiveData<Firm> {
        return currentFirmData
    }
}