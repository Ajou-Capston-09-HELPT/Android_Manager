package com.ajou.helptmanager.network.model

import com.ajou.helptmanager.home.model.EntryLog
import com.google.gson.annotations.SerializedName

data class EntryLogResponse(
    @SerializedName("status") val status : String,
    @SerializedName("data") val data: List<EntryLog>
)
