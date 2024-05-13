package com.ajou.helptmanager.network.api

import com.ajou.helptmanager.network.model.UserMembershipResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GymAdmissionService {
    @POST("gym-admissions/{gymAdmissionId}/approve")
    suspend fun approveUser(
        @Header("Authorization") accessToken : String,
        @Path("gymAdmissionId") id : Int,
        @Query("endDate") endDate : String
    ): Response<UserMembershipResponse>

}