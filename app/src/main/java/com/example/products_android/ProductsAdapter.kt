package com.example.products_android

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductsAdapter (
    val callback: (id: Int) -> Unit
        ) : RecyclerView.Adapter<ProductsViewHolder>() {
    var listProducts = mutableListOf<Product>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product,
                parent,
                false)
        return ProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = listProducts[position]
        //holder.bind(product) { id -> Log.d("Value of id: ", id.toString()) }
        holder.bind(product, callback)
    }

    override fun getItemCount(): Int {
        return listProducts.size
    }

    fun reloadData(products: List<Product>) {
        listProducts.clear()
        listProducts.addAll(products)

        notifyDataSetChanged()
    }
}