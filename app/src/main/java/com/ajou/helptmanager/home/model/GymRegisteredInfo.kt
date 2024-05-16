package com.ajou.helptmanager.home.model

import com.google.gson.annotations.SerializedName

data class GymRegisteredInfo(
    @SerializedName("gymId") val gymId : Int,
    @SerializedName("address") val address : GymAddress,
    @SerializedName("gymName") val gymName : String,
    @SerializedName("status") val status : String
)
