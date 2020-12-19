package com.example.products_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.products_android.databinding.ActivityProductDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonSaveUpdateProduct.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(ProductsService::class.java)
            val id = intent.getIntExtra("PRODUCT_ID_KEY", 0)
            val productUpdateRequest = ProductUpdateRequest(binding.editTextNameProduct.text.toString())
            service.updateProduct(id, productUpdateRequest).enqueue(object: Callback<Response<Product>> {
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

        binding.buttonDeleteProduct.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(ProductsService::class.java)
            val id = intent.getIntExtra("PRODUCT_ID_KEY", 0)
            service.deleteProduct(id).enqueue(object: Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: retrofit2.Response<Unit>) {
                    println(response)
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    println(t.message)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ProductsService::class.java)
        val id = intent.getIntExtra("PRODUCT_ID_KEY", 0)
        service.getProduct(id).enqueue(object: Callback<Response<Product>> {
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
}