package com.example.products_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.products_android.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val adapter = ProductsAdapter { id: Int ->
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("PRODUCT_ID_KEY", id)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //val name: String = "asdf"

        //this.orale()

        //val fullName = name.agregarApellido("ortega") // alexisortega

        //1.multiplicar(3)

        binding.recyclerViewProducts.adapter = adapter

        binding.floatingActionButtonAddNewProduct.setOnClickListener {
            val intent = Intent(this, CreateNewProductActivity::class.java)
            startActivity(intent)
        }
    }

    //fun AppCompatActivity.orale(): String = "asdfsdf"

//    fun Int.multiplicar(n: Int): Int {
//        return this * n
//    }
//
//    fun String.agregarApellido(apellido: String): String {
//        return this + apellido
//    }

    override fun onResume() {
        super.onResume()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ProductsService::class.java)

        service.getProducts().enqueue(object: Callback<Response<List<Product>>> {
            override fun onResponse(
                call: Call<Response<List<Product>>>,
                response: retrofit2.Response<Response<List<Product>>>
            ) {
                val products = response.body()?.data ?: emptyList()
                adapter.reloadData(products)
            }

            override fun onFailure(call: Call<Response<List<Product>>>, t: Throwable) {
                println(t.message)
            }

        })
    }
}