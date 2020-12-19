package com.example.products_android.services

import com.example.products_android.api.CategoryUpdateRequest
import com.example.products_android.api.NewCategoryRequest
import com.example.products_android.models.Category
import com.example.products_android.models.Response
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