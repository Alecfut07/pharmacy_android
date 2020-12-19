package com.example.products_android.services

import com.example.products_android.api.NewProductRequest
import com.example.products_android.api.ProductUpdateRequest
import com.example.products_android.models.Product
import com.example.products_android.models.Response
import retrofit2.Call
import retrofit2.http.*

interface ProductsService {
    @GET("/products")
    fun getProducts(): Call<Response<List<Product>>>

    @GET("/products/{id}")
    fun getProduct(@Path("id") id: Int): Call<Response<Product>>

    @PUT("/products/{id}")
    fun updateProduct(@Path("id") id: Int, @Body productUpdateRequest: ProductUpdateRequest): Call<Response<Product>>

    @POST("/products")
    fun createNewProduct(@Body newProductRequest: NewProductRequest): Call<Response<Product>>

    @DELETE("/products/{id}")
    fun deleteProduct(@Path("id") id: Int): Call<Unit>
}