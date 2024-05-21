package com.ajou.helptmanager.network.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface QrService {
    @POST("qr/verify/gyms/{gymId}")
    suspend fun validateQr(
        @Header("Authorization") qrToken : String,
        @Path("gymId") id : Int
    ): Response<ResponseBody>
}