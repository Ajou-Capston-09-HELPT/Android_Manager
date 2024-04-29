package com.ajou.helptmanager.network.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface ManagerService {
//    @POST("members/update")
//    fun updateMember(
//
//    )
//    @POST("members/register")
//    fun registerMember(
//
//    )
    @POST("managers/login")
    suspend fun login(
        @Body kakaoId : RequestBody
    ) : Response<ResponseBody>

    @DELETE("managers")
    suspend fun quit(
        @Header("Authorization") accessToken : String
    ):Response<ResponseBody>
}