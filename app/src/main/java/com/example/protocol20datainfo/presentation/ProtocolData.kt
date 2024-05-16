package com.example.protocol20datainfo.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProtocolData(
    val stx1: Byte? = null,
    val stx2: Byte? = null,
    val command: Byte? = null,
    val productId1: Byte? = null,
    val productId2: Byte? = null,
    val convertedProductId1: String? = null,
    val convertedProductId2: String? = null,
    val time1: Byte? = null,
    val time2: Byte? = null,
    val time3: Byte? = null,
    val time4: Byte? = null,
    val time5: Byte? = null,
    val time6: Byte? = null,
    val temperature: Double? = null,
    val battery: Double? = null,
    val count: Int? = null,
    val deviceName: String? = null,
    val firmwareVersion: Int? = null
    ) : Parcelable


