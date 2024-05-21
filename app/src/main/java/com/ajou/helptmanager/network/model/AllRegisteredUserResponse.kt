package com.ajou.helptmanager.network.model

import com.ajou.helptmanager.home.model.RegisteredUserInfo
import com.google.gson.annotations.SerializedName

data class AllRegisteredUserResponse(
    @SerializedName("status") val status : String,
    @SerializedName("data") val data : List<RegisteredUserInfo>
)
