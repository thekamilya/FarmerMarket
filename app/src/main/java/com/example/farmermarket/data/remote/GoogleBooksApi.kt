package com.example.farmermarket.data.remote

import com.example.farmermarket.data.remote.dto.BooksResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {

    @GET("volumes")
    suspend fun get_books(@Query("q") q: String = "harry",
                          @Query("startIndex") start_index: Int = 0,
                          @Query("maxResult") max_result: Int = 10): BooksResponseDto
}