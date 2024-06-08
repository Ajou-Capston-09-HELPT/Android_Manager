package com.ajou.helptmanager.network.model

import com.ajou.helptmanager.home.model.UserInfo
import com.google.gson.annotations.SerializedName

data class OneMemberResponse(
    @SerializedName("status") val status : String,
    @SerializedName("data") val data : UserInfo
)
