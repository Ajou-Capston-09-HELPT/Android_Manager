package com.ajou.helptmanager.home.model.product

data class GymEquipment(
    val equipmentId: Int,
    val gymId: Int,
    var customCount: Int,
    var customSet: Int,
    var customWeight: Int
)
