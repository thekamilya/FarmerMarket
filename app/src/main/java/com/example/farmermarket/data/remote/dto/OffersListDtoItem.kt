package com.example.farmermarket.data.remote.dto

data class OffersListDtoItem(
    val buyerName: String = "",
    val expirationDate: String = "",
    val id: Int = 0,
    val isAccepted: Any? = null,
    val message: String = "",
    val price: Double = 0.0,
    val product: ProductXXX = ProductXXX(), // Assuming ProductXXX has a default constructor or default values
    val userId: String = "",

)
