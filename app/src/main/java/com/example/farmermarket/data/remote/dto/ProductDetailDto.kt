package com.example.farmermarket.data.remote.dto

data class ProductDetailDto(
    val category: String = "",
    val description: String = "",
    val farmId: Int = 0,
    val id: Int = 0,
    val imageUrls: List<String?> = emptyList(),
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Double= 0.0,
    val unit: String = ""
)