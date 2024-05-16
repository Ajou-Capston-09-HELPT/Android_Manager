package com.ajou.helptmanager.network.api

import com.ajou.helptmanager.network.model.UserMembershipPeriodResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.Path
import retrofit2.http.Query

interface MemberService {

    @GET("members/{memberId}")
    suspend fun getMemberInfo(
        @Header("Authorization") accessToken : String,
        @Path("memberId") memberId : String
    ) : Response<ResponseBody>

    @GET("members/gyms/{gymId}/search")
    suspend fun getRegisteredMembers(
        @Header("Authorization") accessToken : String,
        @Path("gymId") id : Int,
        @Query("name") name : String?
    ): Response<UserMembershipPeriodResponse>

}