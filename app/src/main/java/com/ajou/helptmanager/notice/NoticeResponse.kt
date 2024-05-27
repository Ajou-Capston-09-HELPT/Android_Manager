package com.ajou.helptmanager.notice

import java.util.Date

data class NoticeResponse(
    val noticeId: Int,
    val title: String,
    val content: String,
    val createAt: String,
)
