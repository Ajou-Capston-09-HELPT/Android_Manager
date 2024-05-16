package com.ajou.helptmanager.network.api

import com.ajou.helptmanager.home.model.Equipment
import com.ajou.helptmanager.home.model.GymEquipment
import com.ajou.helptmanager.network.model.OneEquipmentResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GymEquipmentService{
    @GET("gym-equipments/{gymEquipmentId}")
    suspend fun getGymEquipment(
        @Header("Authorization") accessToken : String,
        @Path("gymEquipmentId") id : Int
    ): Response<OneEquipmentResponse>

    @PUT("gym-equipments/{gymEquipmentId}")
    suspend fun editGymEquipmentSetting(
        @Header("Authorization") accessToken : String,
        @Path("gymEquipmentId") id : Int,
        @Body setting : RequestBody
    ):Response<OneEquipmentResponse>

    @POST("gym-equipments")
    suspend fun postGymEquipment(
        @Header("Authorization") accessToken : String,
        @Body equipment : Equipment
    ): Response<ResponseBody>

    @DELETE("gym-equipments/{gymEquipmentId}")
    suspend fun deleteGymEquipment(
        @Header("Authorization") accessToken : String,
        @Path("gymEquipmentId") id : Int
    ) : Response<ResponseBody>
}
