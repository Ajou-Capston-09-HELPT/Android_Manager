package com.ajou.helptmanager.network.api

import com.ajou.helptmanager.home.model.GymEquipment
import com.ajou.helptmanager.network.model.AllEquipmentsResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface EquipmentService {
    @GET("equipments")
    suspend fun getAllEquipments(
        @Header("Authorization") accessToken : String
    ): Response<AllEquipmentsResponse>

    @POST("gym-equipments")
    suspend fun postEquipment(
        @Header("Authorization") accessToken : String,
        @Body equipment : GymEquipment
    ): Response<ResponseBody>

    @GET("equipments/{equipmentId}")
    suspend fun getEquipment(
        @Header("Authorization") accessToken : String,
        @Path("equipmentId") equipmentId : Int
    ): Response<ResponseBody>

}