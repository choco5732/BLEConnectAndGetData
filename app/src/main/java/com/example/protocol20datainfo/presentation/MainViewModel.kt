package com.example.protocol20datainfo.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val _data: MutableLiveData<ProtocolData> = MutableLiveData()
    val data: LiveData<ProtocolData> get() = _data

    private val _test:  MutableLiveData<String> = MutableLiveData()
    val test: LiveData<String> get() = _test


    fun updateData(data: ProtocolData) {
        Log.e("choco", "${data.toString()}")
        _data.postValue(data)
        Log.e("choco", "_data의 값은 : ${_data.value.toString()}")
    }

    fun testFunction(test: String) {
        _test.postValue(test)
        Log.d("choco5732", "앙앙!")
    }
}