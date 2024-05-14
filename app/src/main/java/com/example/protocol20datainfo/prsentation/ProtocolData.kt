package com.example.protocol20datainfo.prsentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProtocolData(
    val stx1: String? = null,
    val stx2: String? = null,
    val command: String? = null,
    val productId1: String? = null,
    val productId2: String? = null,
    val convertedProductId1: String? = null,
    val convertedProductId2: String? = null,
    val time1: String? = null,
    val time2: String? = null,
    val time3: String? = null,
    val time4: String? = null,
    val time5: String? = null,
    val time6: String? = null,
    val temperature: Double? = null,
    val battery: Double? = null
    ) : Parcelable
