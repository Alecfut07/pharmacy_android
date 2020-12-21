package com.example.products_android.products

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.products_android.MainActivity.Companion.PRODUCT_ID_KEY
import com.example.products_android.R
import com.example.products_android.api.ProductUpdateRequest
import com.example.products_android.api.RetrofitFactory
import com.example.products_android.databinding.ActivityProductDetailBinding
import com.example.products_android.models.Category
import com.example.products_android.models.Product
import com.example.products_android.models.Response
import com.example.products_android.services.CategoriesService
import com.example.products_android.services.ProductsService
import retrofit2.Call
import retrofit2.Callback

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    var productsServcice = RetrofitFactory().create(ProductsService::class.java)
    var categoriesService = RetrofitFactory().create(CategoriesService::class.java)
    var mySelectedCategory: Category? = null

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
        getCategories()
    }

    fun updateProduct() {
        val categoryId = mySelectedCategory?.id ?: 0
        val id = intent.getIntExtra(PRODUCT_ID_KEY, 0)
        val productUpdateRequest = ProductUpdateRequest(
            name = binding.editTextNameProduct.text.toString(),
            details = binding.editTextDetailsProduct.text.toString(),
            category_id = categoryId,
            price = binding.editTextPriceProduct.text.toString().toFloat()
        )
        binding.progressBarActivityProductDetail.visibility = View.VISIBLE
        productsServcice.updateProduct(id, productUpdateRequest).enqueue(object :
            Callback<Response<Product>> {
            override fun onResponse(
                call: Call<Response<Product>>,
                response: retrofit2.Response<Response<Product>>
            ) {
                val product = response.body()?.data
                binding.editTextNameProduct.setText(product?.name)
                binding.editTextDetailsProduct.setText(product?.details)
                binding.editTextPriceProduct.setText(product?.price.toString())
                binding.progressBarActivityProductDetail.visibility = View.GONE
                this@ProductDetailActivity.finish()
            }

            override fun onFailure(call: Call<Response<Product>>, t: Throwable) {
                println(t.message)
                binding.progressBarActivityProductDetail.visibility = View.GONE
            }
        })
    }

    fun deleteProduct() {
        val id = intent.getIntExtra(PRODUCT_ID_KEY, 0)
        binding.progressBarActivityProductDetail.visibility = View.VISIBLE
        productsServcice.deleteProduct(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: retrofit2.Response<Unit>) {
                println(response)
                binding.progressBarActivityProductDetail.visibility = View.GONE
                this@ProductDetailActivity.finish()
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                println(t.message)
                binding.progressBarActivityProductDetail.visibility = View.GONE
            }
        })
    }

    fun getCategories() {
        categoriesService.getCategories().enqueue(object : Callback<Response<List<Category>>> {
            override fun onResponse(
                call: Call<Response<List<Category>>>,
                response: retrofit2.Response<Response<List<Category>>>
            ) {
                val categories = response.body()?.data ?: emptyList()
//                val namesCategories: MutableList<String> = mutableListOf()
//                for (category in categories) {
//                    namesCategories.add(category.name)
//                }
                binding.progressBarActivityProductDetail.visibility = View.GONE
                val adapter = ArrayAdapter(
                    this@ProductDetailActivity,
                    R.layout.list_item,
                    categories
                )
                (binding.spinnerCategoryIdProduct as? AutoCompleteTextView)?.setAdapter(adapter)
                getProductById()

                binding.spinnerCategoryIdProduct.onItemClickListener =
                    object : AdapterView.OnItemClickListener {
                        override fun onItemClick(
                            p0: AdapterView<*>?,
                            p1: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedCategory = categories[position]
                            mySelectedCategory = selectedCategory
                        }
                    }
            }

            override fun onFailure(call: Call<Response<List<Category>>>, t: Throwable) {
                println("FAILURE")
                binding.progressBarActivityProductDetail.visibility = View.GONE
            }

        })
    }

    fun getProductById() {
        binding.progressBarActivityProductDetail.visibility = View.VISIBLE
        val id = intent.getIntExtra(PRODUCT_ID_KEY, 0)
        productsServcice.getProduct(id).enqueue(object : Callback<Response<Product>> {
            override fun onResponse(
                call: Call<Response<Product>>,
                response: retrofit2.Response<Response<Product>>
            ) {
                val product = response.body()?.data
                binding.editTextNameProduct.setText(product?.name)
                binding.editTextDetailsProduct.setText(product?.details)
                binding.editTextPriceProduct.setText(product?.price.toString())
                binding.spinnerCategoryIdProduct.setText(product?.category?.name, false)
                binding.progressBarActivityProductDetail.visibility = View.GONE

                mySelectedCategory = product?.category
            }

            override fun onFailure(call: Call<Response<Product>>, t: Throwable) {
                println(t.message)
                binding.progressBarActivityProductDetail.visibility = View.GONE
            }

        })
    }
}