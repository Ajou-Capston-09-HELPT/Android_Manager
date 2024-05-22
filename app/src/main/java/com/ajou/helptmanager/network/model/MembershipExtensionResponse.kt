package com.ajou.helptmanager.network.model

import com.google.gson.annotations.SerializedName

data class MembershipExtensionResponse(
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String
)