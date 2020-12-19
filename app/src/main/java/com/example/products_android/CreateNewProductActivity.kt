package com.example.products_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateNewProductActivity : AppCompatActivity() {
    private val nameEditText by lazy { findViewById<EditText>(R.id.edit_text_name_product) }
    private val createButton by lazy { findViewById<Button>(R.id.button_create_new_product) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_product)

        createButton.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(ProductsService::class.java)
            val details = ""
            val category_id = 1
            val stock = 0
            val stock_min = 0
            val stock_max = 0
            val price = 0.0f
            val newProductRequest = NewProductRequest(nameEditText.text.toString(), details, category_id, stock, stock_min, stock_max, price)
            service.createNewProduct(newProductRequest).enqueue(object:
                Callback<Response<Product>> {
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
}