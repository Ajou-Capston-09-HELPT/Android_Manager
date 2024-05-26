package com.ajou.helptmanager.home.model

import com.ajou.helptmanager.home.model.GymAddress

data class RegisterGymInfo(
    val address: GymAddress,
    val gymName: String,
    val contactNumber: String,
    val businessNumber: String,
    val businessType: String,
    val ownerName: String
)
