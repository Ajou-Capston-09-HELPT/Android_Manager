package com.ajou.helptmanager.home.model

data class GymEquipment(
    val gymEquipmentId: Int,
    val equipmentName: String,
    var customCount: Int,
    var customSet: Int,
    var customWeight: Int
)
