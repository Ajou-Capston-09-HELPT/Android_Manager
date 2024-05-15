package com.ajou.helptmanager.network.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.Path

interface MemberService {

    @GET("members/{memberId}")
    suspend fun getMemberInfo(
        @Header("Authorization") accessToken : String,
        @Path("memberId") memberId : String
    ) : Response<ResponseBody>

}