package com.example.products_android

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductDetailActivity : AppCompatActivity() {
    private val nameTextView by lazy { findViewById<TextView>(R.id.text_view_name_product) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
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
                nameTextView.text = product?.name
            }

            override fun onFailure(call: Call<Response<Product>>, t: Throwable) {
                println(t.message)
            }

        })
    }
}