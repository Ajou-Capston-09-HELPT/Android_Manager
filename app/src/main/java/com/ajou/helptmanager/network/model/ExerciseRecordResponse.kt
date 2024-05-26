package com.ajou.helptmanager.network.model

import com.ajou.helptmanager.memberDetail.ExerciseRecordData

data class ExerciseRecordResponse(
    val status: Int,
    val data: List<ExerciseRecordData>
)