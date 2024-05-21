package com.ajou.helptmanager.home.view.fragment

import com.ajou.helptmanager.home.model.UserInfo
import com.google.gson.annotations.SerializedName

data class OneMemberResponse(
    @SerializedName("status") val status : String,
    @SerializedName("data") val data : UserInfo
)
