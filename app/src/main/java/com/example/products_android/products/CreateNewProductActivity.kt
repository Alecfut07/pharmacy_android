package com.example.products_android.products

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.products_android.R
import com.example.products_android.api.NewProductRequest
import com.example.products_android.api.RetrofitFactory
import com.example.products_android.databinding.ActivityCreateNewProductBinding
import com.example.products_android.models.Category
import com.example.products_android.models.Product
import com.example.products_android.models.Response
import com.example.products_android.services.CategoriesService
import com.example.products_android.services.ProductsService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import java.lang.NumberFormatException

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
            //createProduct()
            validateProduct()
//            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.progressBarActivityCreateNewProduct.visibility = View.VISIBLE
        getCategories()
    }

    fun createProduct() {
        val categoryId = mySelectedCategory?.id ?: 0
        val newProductRequest = NewProductRequest(
            name = binding.editTextNameProduct.text.toString(),
            details = binding.editTextDetailsProduct.text.toString(),
            categoryId = categoryId,
            price = binding.editTextPriceProduct.text.toString().toFloat()
        )
        binding.progressBarActivityCreateNewProduct.visibility = View.VISIBLE
//        Thread.sleep(2000)
        productsService.createNewProduct(newProductRequest).enqueue(object:
            Callback<Response<Product>> {
            override fun onResponse(
                call: Call<Response<Product>>,
                response: retrofit2.Response<Response<Product>>
            ) {
                if (response.isSuccessful) {
                    val product = response.body()?.data
                    binding.editTextNameProduct.setText(product?.name)
                    this@CreateNewProductActivity.finish()
                } else {
                    MaterialAlertDialogBuilder(this@CreateNewProductActivity)
                        .setTitle("Hola")
                        .setMessage("que onda")
                        .setPositiveButton("OK", null)
                        .create()
                        .show()
                }
                binding.progressBarActivityCreateNewProduct.visibility = View.GONE
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

    fun validateProduct() {
        val isNameValid = binding.editTextNameProduct.text.toString().isNotEmpty()
        binding.nameProductLayout.error = if (isNameValid) {
            null
        } else {
            "Name is required"
        }

        val isDetailsValid = binding.editTextDetailsProduct.text.toString().isNotEmpty()
        binding.detailsProductLayout.error = if (isDetailsValid) {
            null
        } else {
            "Details is required"
        }

        val isCategoryValid = mySelectedCategory != null
        binding.spinnerCategoryIdProductLayout.error = if (isCategoryValid) {
            null
        } else {
            "Category is required"
        }

        val isPriceValid = validatePriceNumber()
        binding.priceProductLayout.error = if (isPriceValid) {
            null
        } else {
            "String is not a number"
        }

        if (isNameValid && isDetailsValid && isCategoryValid && isPriceValid) {
            createProduct()
        }
    }

    fun validatePriceNumber(): Boolean {
        var isNumber = true
        try {
            binding.editTextPriceProduct.text.toString().toFloat()
        } catch (e: NumberFormatException) {
            isNumber = false
        }
        return isNumber
    }
}