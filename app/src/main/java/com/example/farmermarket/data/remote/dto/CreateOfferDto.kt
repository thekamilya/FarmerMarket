package com.example.farmermarket.data.remote.dto

data class CreateOfferDto(
    val message: String,
    val price: Int,
    val productId: Int
)