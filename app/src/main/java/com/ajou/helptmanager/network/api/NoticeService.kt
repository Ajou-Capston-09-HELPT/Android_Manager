package com.ajou.helptmanager.network.api

import com.ajou.helptmanager.network.model.AllNoticeResponse
import com.ajou.helptmanager.notice.NoticeRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NoticeService {
    @GET("/notices")
    suspend fun getNoticeList(
        @Header("Authorization") accessToken: String,
        @Query("gymId") gymId: Int?
    ): Response<AllNoticeResponse>

    @POST("/notices/upload")
    suspend fun uploadNotice(
        @Header("Authorization") accessToken: String,
        @Body noticeRequest: NoticeRequest
    ): Response<ResponseBody>

    @POST("/notices/{noticeId}/delete")
    suspend fun deleteNotice(
        @Header("Authorization") accessToken: String,
        @Path("noticeId") noticeId: Int
    ): Response<ResponseBody>

    @PUT("/notices/{noticeId}/modify")
    suspend fun modifyNotice(
        @Header("Authorization") accessToken: String,
        @Path("noticeId") noticeId: Int,
        @Body noticeRequest: NoticeRequest
    ): Response<ResponseBody>
}