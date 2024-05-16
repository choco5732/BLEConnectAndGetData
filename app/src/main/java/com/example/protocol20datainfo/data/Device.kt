package com.example.protocol20datainfo.data

import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Device(
    val deviceName : String? = null,
    val deviceMac : String? = null,
    val device : BluetoothDevice? = null,
    var isConnect : Boolean = false
) : Parcelable