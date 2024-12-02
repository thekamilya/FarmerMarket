package com.example.farmermarket.data.remote.dto

data class ProductX(
    val category: String,
    val id: Int,
    val name: String,
    val quantity: Double,
    val soldDate: String,
    val soldPrice: Double
)

//This data class is a part of OrdersDtoItem