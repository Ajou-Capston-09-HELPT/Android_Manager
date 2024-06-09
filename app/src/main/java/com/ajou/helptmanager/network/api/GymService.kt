package com.ajou.helptmanager.network.api

import com.ajou.helptmanager.network.model.GymRegisteredInfoResponse
import com.ajou.helptmanager.home.model.RegisterGymInfo
import com.ajou.helptmanager.network.model.AllGymEquipmentsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface GymService {
    @Multipart
    @POST("gyms")
    suspend fun register(
        @Header("Authorization") accessToken : String,
        @Part("gymInfo") gymInfo : RegisterGymInfo,
        @Part file : MultipartBody.Part
    ) : Response<ResponseBody>

    @GET("gyms")
    suspend fun searchGym(
        @Header("Authorization") accessToken : String,
        @Query("name") name : String?
    ) : Response<ResponseBody>

    @GET("gyms/{gym_id}")
    suspend fun getGymInfo(
        @Header("Authorization") accessToken : String,
        @Path("gym_id") gymId : Int
    ) : Response<GymRegisteredInfoResponse>

    @GET("gyms/status")
    suspend fun getGymStatus(
        @Header("Authorization") accessToken : String
    ) : Response<ResponseBody>

    @GET("gyms/{gymId}/gym-equipments")
    suspend fun getAllGymEquipments(
        @Header("Authorization") accessToken : String,
        @Path("gymId") gymId : Int
    ):Response<AllGymEquipmentsResponse>

    @PUT("gyms/{gymId}/chat-link")
    suspend fun putChatLink(
        @Header("Authorization") accessToken : String,
        @Path("gymId") gymId : Int,
        @Body body : RequestBody
    ): Response<ResponseBody>

    @GET("gyms/{gymId}/chat-link")
    suspend fun getChatLink(
        @Header("Authorization") accessToken : String,
        @Path("gymId") gymId : Int
    ): Response<ResponseBody>

    @DELETE("gyms/{gymId}/chat-link")
    suspend fun deleteChatLink(
        @Header("Authorization") accessToken : String,
        @Path("gymId") gymId : Int
    ): Response<ResponseBody>


}