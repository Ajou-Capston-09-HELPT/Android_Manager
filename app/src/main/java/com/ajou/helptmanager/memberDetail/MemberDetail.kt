package com.ajou.helptmanager.memberDetail

data class MemberDetail(
    val userName: String,
    val gender: String,
    val height: String,
    val weight: String,
    val membershipId: Int,
    val startDate: String,
    val endDate: String,
    val profileImage: String,
    val birthDate: String
)
