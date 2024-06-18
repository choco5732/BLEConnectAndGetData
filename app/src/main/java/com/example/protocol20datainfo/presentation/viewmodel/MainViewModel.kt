package com.example.protocol20datainfo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.protocol20datainfo.data.ProtocolData

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
}