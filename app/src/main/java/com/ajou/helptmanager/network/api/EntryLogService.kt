package com.ajou.helptmanager.network.api

import com.ajou.helptmanager.network.model.EntryLogResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

interface EntryLogService {
    @GET("entry-logs")
    suspend fun getEntryLogs(
        @Header("Authorization") accessToken : String,
        @Query("name") name : String,
        @Query("gymId") gymId : Int,
        @Query("date") date : LocalDate
    ): Response<EntryLogResponse>
}