package com.ajou.helptmanager.memberDetail

data class ExerciseRecordData(
    val equipmentId: Int,
    val count: Int,
    val setNumber: Int,
    val weight: Int,
    val startdate: String,
    val enddate: String,
    val recordDate: String,
    val successRate: Float
)