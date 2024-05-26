package com.ajou.helptmanager.network.api

import com.ajou.helptmanager.network.model.AllPendingUsersResponse
import com.ajou.helptmanager.network.model.UserMembershipResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

interface GymAdmissionService {
    @POST("gym-admissions/{gymAdmissionId}/approve")
    suspend fun approveUser(
        @Header("Authorization") accessToken : String,
        @Path("gymAdmissionId") id : Int,
        @Query("endDate") endDate : LocalDate
    ): Response<UserMembershipResponse>

    @GET("gym-admissions/gyms/{gymId}")
    suspend fun getAllAdmissionUsers(
        @Header("Authorization") accessToken : String,
        @Path("gymId") id : Int,
    ) : Response<AllPendingUsersResponse>

    @DELETE("gym-admissions/{gymAdmissionId}/reject")
    suspend fun rejectUser(
        @Header("Authorization") accessToken : String,
        @Path("gymAdmissionId") id : Int
    ):Response<ResponseBody>

}