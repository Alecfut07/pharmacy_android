package com.example.products_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private val adapter = ProductsAdapter { id: Int ->
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("PRODUCT_ID_KEY", id)
        startActivity(intent)
    }
    private val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recycler_view_products)
    }

    private val floatingActionButton by lazy { findViewById<FloatingActionButton>(R.id.floating_action_button_add_new_product) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val name: String = "asdf"

        //this.orale()

        //val fullName = name.agregarApellido("ortega") // alexisortega

        //1.multiplicar(3)

        recyclerView.adapter = adapter

        floatingActionButton.setOnClickListener {
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
            .baseUrl("http://10.0.2.2:3000")
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