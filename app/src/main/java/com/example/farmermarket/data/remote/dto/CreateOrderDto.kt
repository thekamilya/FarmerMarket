package com.example.farmermarket.data.remote.dto

data class CreateOrderDto(
    val buyerId: String,
    val products: List<Product>
)

//This data class is a part of CreateOrderDto