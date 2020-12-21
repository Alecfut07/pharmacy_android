package com.example.products_android.api

import com.google.gson.annotations.SerializedName

class NewProductRequest(
    val name: String,
    val details: String,
    @SerializedName("category_id")
    val categoryId: Int,
    val price: Float,
    val stock: Int = 0,
    @SerializedName("stock_min")
    val stockMin: Float = 0.0f,
    @SerializedName("stock_max")
    val stockMax: Float = 0.0f
)