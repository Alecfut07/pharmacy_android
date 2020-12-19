package com.example.products_android

import retrofit2.Call
import retrofit2.http.*

interface CategoriesService {
    @GET("/categories")
    fun getCategories(): Call<Response<List<Category>>>

    @GET("/categories/{id}")
    fun getCategory(@Path("id") id: Int): Call<Response<Category>>

    @PUT("/categories/{id}")
    fun updateCategory(@Path("id") id: Int, @Body categoryUpdateRequest: CategoryUpdateRequest): Call<Response<Category>>

    @POST("/categories")
    fun createNewCategory(@Body newCategoryRequest: NewCategoryRequest): Call<Response<Category>>

    @DELETE("/categories/{id}")
    fun deleteCategory(@Path("id") id: Int): Call<Unit>
}

//class Response<T>(val data: T? = null)

class Category(
    val id: Int,
    val name: String
)