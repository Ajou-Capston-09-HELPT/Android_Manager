package com.ajou.helptmanager.home.model

data class PendingUserInfo(
    val memberId : Int,
    val gymAdmissionId: Int,
    val userName: String,
    val gender : String,
    val height : Int,
    val weight : Int,
    var startDate : String?,
    var endDate : String?,
    val membershipId : Int,
    val profileImage: String,
    val birthDate: String
)
