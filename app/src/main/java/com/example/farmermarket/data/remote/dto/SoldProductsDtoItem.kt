package com.example.farmermarket.data.remote.dto

data class SoldProductsDtoItem(
    val category: String = "",
    val farmId: String ="",
    val id: Int = 0,
    val name: String = "",
    val buyerId : String? = "",
    val buyerName: String? = "",
    val quantity: Double = 0.0,
    val soldDate: String = "",
    val soldPrice: Double = 0.0,
    val status: String =""
)