package com.ajou.helptmanager.membership.model

data class ApiResponse<T>(
    val status: Int,
    val data: T?,
    val message: String?
)