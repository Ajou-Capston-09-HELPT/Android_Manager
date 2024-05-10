package com.ajou.helptmanager.home.model

data class Equipment(
    val equipmentId: Int,
    val equipmentName: String,
    var defaultCount: Int,
    var defaultSet: Int,
    var defaultWeight: Int
)
