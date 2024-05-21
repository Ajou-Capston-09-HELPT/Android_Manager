package com.ajou.helptmanager.network.model

import com.ajou.helptmanager.home.model.PendingUserInfo
import com.google.gson.annotations.SerializedName

data class AllPendingUsersResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data : List<PendingUserInfo>
)
