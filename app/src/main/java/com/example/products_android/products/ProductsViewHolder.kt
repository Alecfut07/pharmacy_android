package com.example.products_android.products

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.products_android.databinding.ItemProductBinding
import com.example.products_android.models.Product

class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    private val productTitle = itemView.findViewById<TextView>(R.id.product_tittle)

    private var binding = ItemProductBinding.bind(itemView)

    private var product: Product? = null

    fun bind(product: Product, callback: (id: Int) -> Unit) {
        this.product = product

        binding.productTittle.text = product.name
        binding.productCategory.text = product.category.name
        binding.productPrice.text = "$${product.price.toString()}"
        itemView.setOnClickListener {
            if (this.product != null) {
                callback(this.product!!.id)
            }

//            this.product?.let { callback(it.id) }
        }
    }

}