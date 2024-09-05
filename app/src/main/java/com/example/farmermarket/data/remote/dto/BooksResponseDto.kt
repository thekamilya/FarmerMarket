package com.example.farmermarket.data.remote.dto

data class BooksResponseDto(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)