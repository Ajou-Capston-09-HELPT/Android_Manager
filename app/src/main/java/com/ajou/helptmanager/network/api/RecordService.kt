package com.ajou.helptmanager.network.api

import com.ajou.helptmanager.network.model.ExerciseRecordResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RecordService {
    @GET("records/calender")
    suspend fun getExerciseRecordDate(
        @Header("Authorization") accessToken: String,
        @Query("date") date: String
    ): Response<ExerciseRecordResponse>


    @POST("records/calender/members/{memberId}")
    suspend fun getExerciseRecordDateByMemberId(
        @Header("Authorization") accessToken: String,
        @Path("memberId") memberId: Int?,
        @Query("date") date: String
    ): Response<ResponseBody>

}