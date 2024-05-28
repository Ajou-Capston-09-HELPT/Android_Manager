package com.ajou.helptmanager.notice

data class NoticeUploadRequest(
    val gymId: Int,
    val title: String,
    val content: String,
    val createAt: String
)
