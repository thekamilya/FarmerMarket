package com.example.farmermarket.domain.usecase

import android.util.Log
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.ProductResponseDto
import com.example.farmermarket.domain.repository.FarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: FarmRepository
) {
    operator fun invoke(): Flow<Resource<ProductResponseDto>> = flow {
        try {
            emit(Resource.Loading())
            val products = repository.getProducts()
            emit(Resource.Success<ProductResponseDto>(products))
            Log.d("kama", products.toString())
        } catch (e: HttpException) {
            emit(Resource.Error<ProductResponseDto>(e.localizedMessage ?: "An unexpected error occured"))
            Log.d("kama", e.message.toString())
        } catch (e: IOException) {
            Log.d("kama", e.message.toString())
            emit(Resource.Error<ProductResponseDto>(e.message.toString()))
        }
    }
}