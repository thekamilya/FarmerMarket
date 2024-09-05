package com.example.farmermarket.domain.repository

import com.example.farmermarket.data.remote.dto.BooksResponseDto


interface BookRepository {

    suspend fun getBooks(): BooksResponseDto
}