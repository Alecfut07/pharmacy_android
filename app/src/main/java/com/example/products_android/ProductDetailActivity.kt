package com.example.products_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductDetailActivity : AppCompatActivity() {
    private val nameEditText by lazy { findViewById<EditText>(R.id.edit_text_name_product) }
    private val saveButton by lazy { findViewById<Button>(R.id.button_save_update_product) }
    private val deleteButton by lazy { findViewById<Button>(R.id.button_delete_product) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        saveButton.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(ProductsService::class.java)
            val id = intent.getIntExtra("PRODUCT_ID_KEY", 0)
            val productUpdateRequest = ProductUpdateRequest(nameEditText.text.toString())
            service.updateProduct(id, productUpdateRequest).enqueue(object: Callback<Response<Product>> {
                override fun onResponse(
                    call: Call<Response<Product>>,
                    response: retrofit2.Response<Response<Product>>
                ) {
                    val product = response.body()?.data
                    nameEditText.setText(product?.name)
                }

                override fun onFailure(call: Call<Response<Product>>, t: Throwable) {
                    println(t.message)
                }
            })
        }

        deleteButton.setOnClickListener {
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
                nameEditText.setText(product?.name)
            }

            override fun onFailure(call: Call<Response<Product>>, t: Throwable) {
                println(t.message)
            }

        })
    }
}