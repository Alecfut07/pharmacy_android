package com.example.products_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.products_android.databinding.ActivityCreateNewProductBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateNewProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewProductBinding
    var productsService = RetrofitFactory().create(ProductsService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonCreateNewProduct.setOnClickListener {
            val details = ""
            val category_id = 1
            val stock = 0
            val stock_min = 0
            val stock_max = 0
            val price = 0.0f
            val newProductRequest = NewProductRequest(binding.editTextNameProduct.text.toString(), details, category_id, stock, stock_min, stock_max, price)
            productsService.createNewProduct(newProductRequest).enqueue(object:
                Callback<Response<Product>> {
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
}