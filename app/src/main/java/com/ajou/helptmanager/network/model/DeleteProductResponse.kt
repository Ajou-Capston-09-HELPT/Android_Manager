package com.ajou.helptmanager.network.model


import com.google.gson.annotations.SerializedName

data class DeleteProductResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: Boolean
)