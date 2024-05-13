package com.ajou.helptmanager.home.model

data class Equipment(
    val equipmentId: Int,
    val equipmentName: String,
    var customCount: Int,
    var customSet: Int,
    var customWeight: Int
)
