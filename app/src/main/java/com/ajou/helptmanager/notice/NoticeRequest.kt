package com.ajou.helptmanager.notice

data class NoticeRequest(
    val noticeId: Int,
    val gymId: Int,
    val title: String,
    val content: String,
    val createdAt: String
)
