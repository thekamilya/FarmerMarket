package com.example.farmermarket.data.repository

import com.example.farmermarket.data.remote.GoogleBooksApi
import com.example.farmermarket.data.remote.dto.BooksResponseDto
import com.example.farmermarket.domain.repository.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api: GoogleBooksApi
): BookRepository {
    override suspend fun getBooks(): BooksResponseDto {
        return api.get_books()
    }

}