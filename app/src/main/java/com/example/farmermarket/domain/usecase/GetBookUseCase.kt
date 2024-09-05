package com.example.farmermarket.domain.usecase

import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.Item
import com.example.farmermarket.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetBookUseCase  @Inject constructor(
    private val repository: BookRepository
) {
    operator fun invoke(): Flow<Resource<List<Item>>> = flow {
        try {
            emit(Resource.Loading())
            val book = repository.getBooks()
            emit(Resource.Success<List<Item>>(book.items))
        } catch (e: HttpException) {
            emit(Resource.Error<List<Item>>(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error<List<Item>>(e.message.toString()))
        }
    }
}