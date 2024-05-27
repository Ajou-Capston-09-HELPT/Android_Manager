package com.ajou.helptmanager.notice

data class NoticeModifyRequest(
    val noticeId: Int,
    val gymId: Int,
    val title: String,
    val content: String,
    val createAt: String
)
