package com.ajou.helptmanager.network.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MembershipControllerService {
    @PUT("/memberships/{membershipId}/extension")
    suspend fun extendMembership(
        @Header("Authorization") accessToken: String,
        @Path("membershipId") membershipId: Int,
        @Query("endDate") endDate: String
    ): Response<ResponseBody>

}