package com.example.products_android.products

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
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
            createProduct()
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
        val price = 0.0f
        val newProductRequest = NewProductRequest(binding.editTextNameProduct.text.toString(), binding.editTextDetailsProduct.text.toString(), category_id, stock, stock_min, stock_max, binding.editTextPriceProduct.text.toString().toFloat())
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
                val adapter = ArrayAdapter(this@CreateNewProductActivity, android.R.layout.simple_spinner_item, categories)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCategoryIdProduct.adapter = adapter
                binding.spinnerCategoryIdProduct.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                        val selectedCategory = categories[position]
                        mySelectedCategory = selectedCategory
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
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