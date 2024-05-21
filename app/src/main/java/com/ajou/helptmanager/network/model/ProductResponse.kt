package com.ajou.helptmanager.network.model

import com.ajou.helptmanager.home.model.ProductResponseData
import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: ProductResponseData
)