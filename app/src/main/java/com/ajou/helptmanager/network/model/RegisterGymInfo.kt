package com.ajou.helptmanager.network.model

data class RegisterGymInfo(
    val address: GymAddress,
    val gymName: String,
    val contactNumber: String,
    val businessNumber: String,
    val businessType: String,
    val ownerName: String
)
