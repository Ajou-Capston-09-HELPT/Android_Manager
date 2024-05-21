package com.ajou.helptmanager.network.model

import com.ajou.helptmanager.home.model.GymEquipment
import com.google.gson.annotations.SerializedName

data class AllGymEquipmentsResponse(
    @SerializedName("status") val status : String,
    @SerializedName("data") val data: List<GymEquipment>
)
