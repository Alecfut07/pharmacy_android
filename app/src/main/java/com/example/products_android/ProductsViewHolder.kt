package com.example.products_android

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val productTitle = itemView.findViewById<TextView>(R.id.product_tittle)
    private var product: Product? = null
    fun bind(product: Product, callback: (id: Int) -> Unit) {
        this.product = product
        productTitle.text = product.name
        itemView.setOnClickListener {
            if (this.product != null){
                callback(this.product!!.id)
            }
//            this.product?.let { callback(it.id) }
        }
    }

}