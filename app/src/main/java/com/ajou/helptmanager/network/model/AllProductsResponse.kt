package com.ajou.helptmanager.network.model

import com.ajou.helptmanager.home.model.ProductResponseData
import com.google.gson.annotations.SerializedName

data class AllProductsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<ProductResponseData>
)