package com.ajou.helptmanager.network.api

import com.ajou.helptmanager.memberDetail.DailyRecordsResponse
import com.ajou.helptmanager.network.model.ExerciseRecordResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RecordService {
    @POST("records/detail/members")
    suspend fun getExerciseRecord(
        @Header("Authorization") accessToken: String,
        @Query("memberId") memberId: Int?,
        @Query("date") date: String,
    ): Response<DailyRecordsResponse>

}