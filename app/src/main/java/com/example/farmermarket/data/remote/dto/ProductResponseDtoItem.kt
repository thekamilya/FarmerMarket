package com.example.farmermarket.data.remote.dto

import retrofit2.http.Url

data class ProductResponseDtoItem(
    val category: String = "",
    val description: String = "",
    val farmId: Any = "",
    val id: Int = 0,
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Double = 0.0,
    val unit: String = "",
    val imageURL:String = ""
)