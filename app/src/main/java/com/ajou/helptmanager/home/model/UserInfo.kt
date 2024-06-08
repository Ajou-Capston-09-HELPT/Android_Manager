package com.ajou.helptmanager.home.model

import java.time.LocalDate

data class UserInfo(
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
