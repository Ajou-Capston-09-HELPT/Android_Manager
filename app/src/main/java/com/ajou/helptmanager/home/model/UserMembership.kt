package com.ajou.helptmanager.home.model

data class UserMembership(
    val membershipId: Int,
    val userId: Int,
    val gymId: Int,
    val attendanceDate: Int,
    val isAttendToday: Boolean,
    val startDate: String,
    val endDate: String
)
