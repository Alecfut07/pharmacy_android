package com.example.products_android

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductsService {
    @GET("/products")
    fun getProducts(): Call<Response<List<Product>>>

    @GET("/products/{id}")
    fun getProduct(@Path("id") id: Int): Call<Response<Product>>
}

class Response<T>(val data: T)

class Product(
    val id: Int,
    val name: String
)