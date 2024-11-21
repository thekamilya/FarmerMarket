package com.example.farmermarket.domain.usecase

import android.util.Log
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.data.remote.dto.ProductResponseDto
import com.example.farmermarket.domain.repository.FarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val repository: FarmRepository
) {
    operator fun invoke(id: Int): Flow<Resource<ProductDetailDto>> = flow {
        try {
            emit(Resource.Loading())
            val product = repository.getProduct(id)
            emit(Resource.Success<ProductDetailDto>(product))
            Log.d("kama", product.toString())
        } catch (e: HttpException) {
            emit(Resource.Error<ProductDetailDto>(e.localizedMessage ?: "An unexpected error occured"))
            Log.d("kama", e.message.toString())
        } catch (e: IOException) {
            Log.d("kama", e.message.toString())
            emit(Resource.Error<ProductDetailDto>(e.message.toString()))
        }
    }
}