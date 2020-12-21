package com.example.products_android.api

import com.google.gson.annotations.SerializedName

class ProductUpdateRequest(
    val name: String,
    val details: String,
    @SerializedName("category_id")
    val categoryId: Int,
    val price: Float
)