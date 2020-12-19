package com.example.products_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.products_android.MainActivity.Companion.PRODUCT_ID_KEY
import com.example.products_android.databinding.ActivityProductDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    var productsServcice = RetrofitFactory().create(ProductsService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.buttonSaveUpdateProduct.setOnClickListener {
            updateProduct()
        }

        binding.buttonDeleteProduct.setOnClickListener {
            deleteProduct()
        }
    }

    override fun onResume() {
        super.onResume()
        val id = intent.getIntExtra(PRODUCT_ID_KEY, 0)
        productsServcice.getProduct(id).enqueue(object: Callback<Response<Product>> {
            override fun onResponse(
                call: Call<Response<Product>>,
                response: retrofit2.Response<Response<Product>>
            ) {
                val product = response.body()?.data
                binding.editTextNameProduct.setText(product?.name)
            }

            override fun onFailure(call: Call<Response<Product>>, t: Throwable) {
                println(t.message)
            }

        })
    }

    fun updateProduct() {
        val id = intent.getIntExtra(PRODUCT_ID_KEY, 0)
        val productUpdateRequest = ProductUpdateRequest(binding.editTextNameProduct.text.toString())
        productsServcice.updateProduct(id, productUpdateRequest).enqueue(object: Callback<Response<Product>> {
            override fun onResponse(
                call: Call<Response<Product>>,
                response: retrofit2.Response<Response<Product>>
            ) {
                val product = response.body()?.data
                binding.editTextNameProduct.setText(product?.name)
            }

            override fun onFailure(call: Call<Response<Product>>, t: Throwable) {
                println(t.message)
            }
        })
    }

    fun deleteProduct() {
        val id = intent.getIntExtra(PRODUCT_ID_KEY, 0)
        productsServcice.deleteProduct(id).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: retrofit2.Response<Unit>) {
                println(response)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                println(t.message)
            }
        })
    }
}