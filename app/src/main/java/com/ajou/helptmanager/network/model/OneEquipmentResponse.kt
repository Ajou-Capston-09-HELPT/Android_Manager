package com.ajou.helptmanager.network.model

import com.ajou.helptmanager.home.model.Equipment
import com.google.gson.annotations.SerializedName

data class OneEquipmentResponse(
    @SerializedName("status") val status : String,
    @SerializedName("data") val data: Equipment
)
