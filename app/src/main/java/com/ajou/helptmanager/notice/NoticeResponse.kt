package com.ajou.helptmanager.notice

data class NoticeResponse(
    val noticeId: Int,
    val title: String,
    val content: String,
    val createdAt: String,
)
