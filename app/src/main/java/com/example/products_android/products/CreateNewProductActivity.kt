package com.example.products_android.products

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.products_android.MainActivity
import com.example.products_android.R
import com.example.products_android.api.NewProductRequest
import com.example.products_android.api.RetrofitFactory
import com.example.products_android.databinding.ActivityCreateNewProductBinding
import com.example.products_android.models.Category
import com.example.products_android.models.Product
import com.example.products_android.models.Response
import com.example.products_android.services.CategoriesService
import com.example.products_android.services.ProductsService
import retrofit2.Call
import retrofit2.Callback

class CreateNewProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewProductBinding
    var productsService = RetrofitFactory().create(ProductsService::class.java)
    var categoriesService = RetrofitFactory().create(CategoriesService::class.java)
    var mySelectedCategory: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonCreateNewProduct.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
            createProduct()
//            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.progressBarActivityCreateNewProduct.visibility = View.VISIBLE
        getCategories()
    }

    fun createProduct() {
        val details = ""
        val category_id = mySelectedCategory?.id ?: 0
        val stock = 0
        val stock_min = 0
        val stock_max = 0
        val newProductRequest = NewProductRequest(
            name = binding.editTextNameProduct.text.toString(),
            details = binding.editTextDetailsProduct.text.toString(),
            category_id = category_id,
            stock = stock,
            stock_min = stock_min,
            stock_max = stock_max,
            price = binding.editTextPriceProduct.text.toString().toFloat()
        )
        binding.progressBarActivityCreateNewProduct.visibility = View.VISIBLE
        productsService.createNewProduct(newProductRequest).enqueue(object:
            Callback<Response<Product>> {
            override fun onResponse(
                call: Call<Response<Product>>,
                response: retrofit2.Response<Response<Product>>
            ) {
                val product = response.body()?.data
                binding.editTextNameProduct.setText(product?.name)
                binding.progressBarActivityCreateNewProduct.visibility = View.GONE
                this@CreateNewProductActivity.finish()
            }

            override fun onFailure(call: Call<Response<Product>>, t: Throwable) {
                println(t.message)
                binding.progressBarActivityCreateNewProduct.visibility = View.GONE
            }
        })
    }

    fun getCategories() {
        categoriesService.getCategories().enqueue(object: Callback<Response<List<Category>>> {
            override fun onResponse(
                call: Call<Response<List<Category>>>,
                response: retrofit2.Response<Response<List<Category>>>
            ) {
                val categories = response.body()?.data ?: emptyList()
//                val namesCategories: MutableList<String> = mutableListOf()
//                for (category in categories) {
//                    namesCategories.add(category.name)
//                }
                binding.progressBarActivityCreateNewProduct.visibility = View.GONE
                val adapter = ArrayAdapter(this@CreateNewProductActivity, R.layout.list_item, categories)
                (binding.spinnerCategoryIdProduct as? AutoCompleteTextView)?.setAdapter(adapter)

                binding.spinnerCategoryIdProduct.onItemClickListener = object : AdapterView.OnItemClickListener {
                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                        val selectedCategory = categories[position]
                        mySelectedCategory = selectedCategory
                    }
                }
            }

            override fun onFailure(call: Call<Response<List<Category>>>, t: Throwable) {
                println("FAILURE")
                binding.progressBarActivityCreateNewProduct.visibility = View.GONE
            }

        })
    }
}