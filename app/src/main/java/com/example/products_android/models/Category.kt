package com.example.products_android.models

class Category(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return this.name
    }
}