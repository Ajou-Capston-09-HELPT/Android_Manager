package com.ajou.helptmanager.network.model

import com.ajou.helptmanager.home.model.Membership
import com.ajou.helptmanager.home.model.UserMembership
import com.google.gson.annotations.SerializedName

data class UserMembershipResponse(
    @SerializedName("status") val status : Int,
    @SerializedName("data") val data : UserMembership
)
