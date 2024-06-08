package com.ajou.helptmanager.network.api

import com.ajou.helptmanager.network.model.OneMemberResponse
import com.ajou.helptmanager.network.model.AllRegisteredUserResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MemberService {
    @GET("members/gyms/{gymId}/search")
    suspend fun getRegisteredMembers(
        @Header("Authorization") accessToken : String,
        @Path("gymId") id : Int,
        @Query("name") name : String?
    ): Response<AllRegisteredUserResponse>

    @GET("members/{memberId}")
    suspend fun getOneMemberInfo(
        @Header("Authorization") accessToken : String,
        @Path("memberId") id : Int
    ):Response<OneMemberResponse>

}