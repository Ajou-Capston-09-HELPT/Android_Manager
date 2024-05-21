package com.ajou.helptmanager.network.model

import com.ajou.helptmanager.home.model.GymRegisteredInfo
import com.google.gson.annotations.SerializedName

data class GymRegisteredInfoResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: GymRegisteredInfo
)
