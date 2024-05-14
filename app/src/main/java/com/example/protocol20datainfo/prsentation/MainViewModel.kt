package com.example.protocol20datainfo.prsentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val _data: MutableLiveData<ProtocolData> = MutableLiveData()
    val data: LiveData<ProtocolData> get() = _data


    fun updateData(data: ProtocolData) {
        _data.value = data
    }
}