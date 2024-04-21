package com.example.protocol20datainfo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Device(
    val deviceName : String? = null,
    val deviceMac : String? = null

) : Parcelable