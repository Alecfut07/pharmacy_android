package com.example.products_android.api

class NewProductRequest(
    val name: String,
    val details: String,
    val category_id: Int,
    val stock: Int,
    val stock_min: Int,
    val stock_max: Int,
    val price: Float
)