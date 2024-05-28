package com.ajou.helptmanager.network.model

import com.ajou.helptmanager.notice.NoticeResponse
import com.google.gson.annotations.SerializedName

data class AllNoticeResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<NoticeResponse>
)
