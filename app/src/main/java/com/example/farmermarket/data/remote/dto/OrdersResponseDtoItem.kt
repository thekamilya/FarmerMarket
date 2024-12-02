package com.example.farmermarket.data.remote.dto

data class OrdersResponseDtoItem(
    val buyerId: String ="",
    val buyerName: String ="",
    val createdDate: String ="",
    val id: Int = 0,
    val products: List<ProductXX> = emptyList(),
    val totalPrice: Double = 0.0
)