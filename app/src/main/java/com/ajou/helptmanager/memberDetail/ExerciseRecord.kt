package com.ajou.helptmanager.memberDetail

data class ExerciseRecord(
    val equipmentName: String,
    val count: Int,
    val setNumber: Int,
    val weight: Int,
    val recordTime: String,
    val recordDate: String,
    val successRate: Float,
    val comment: String,
    val snapshotFile: String
)
