package com.ajou.helptmanager.home.model

data class RegisteredUserInfo(
    val userId : Int,
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
