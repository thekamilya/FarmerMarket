package com.example.farmermarket.data.remote.dto

data class FarmerDashBoardDto(
    val income: Double = 0.0,
    val productQuantities: Map<String, Double> = mapOf(),
    val totalQuantity: Double = 0.0,
    val totalSold: Int = 0,
    val totalSolds: Map<String, Int> = mapOf()
)