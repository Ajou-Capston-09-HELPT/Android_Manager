package com.ajou.helptmanager.network.model

import com.ajou.helptmanager.home.model.UserMembership
import com.google.gson.annotations.SerializedName

data class UserMembershipResponse(
    @SerializedName("status") val status : String,
    @SerializedName("data") val data : UserMembership
)
