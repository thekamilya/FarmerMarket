package com.example.farmermarket.data.remote.dto

import retrofit2.http.Url

data class ProductDTO(
    val id: Int = 0,
    val category: String,
    val description: String,
    val name: String,
    val price: Double,
    val quantity: String,
    val unit: String,
    val imageUrl: Url
)