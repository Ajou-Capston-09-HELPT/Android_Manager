package com.ajou.helptmanager.memberDetail

import com.google.gson.annotations.SerializedName

data class DailyRecordsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data : List<ExerciseRecord>
)
