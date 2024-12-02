package com.example.farmermarket.data.remote.dto

data class BuyerDashBoardDto(
    val expenditure: Double = 0.0,
    val productQuantities: Map<String, Double> = mapOf(),
    val totalBoughts: Int = 0,
    val totalQuantity: Double = 0.0
)