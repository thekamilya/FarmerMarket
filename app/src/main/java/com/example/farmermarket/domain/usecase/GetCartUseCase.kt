package com.example.farmermarket.domain.usecase

import android.util.Log
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.Conversation
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.data.repository.DatabaseOperationException
import com.example.farmermarket.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCartUseCase {
    operator fun invoke(userId: String, onRetrieved:(List<ProductDetailDto>)->Unit): Flow<Resource<List<ProductDetailDto>>> = flow {
        try {
            emit(Resource.Loading())
            val repo = FirebaseRepository()
            var productList = emptyList<ProductDetailDto>()
            repo.getCart(userId, onSuccess = {
                onRetrieved(it)
                Log.d("kama", it.toString())
            }, onFailure = {

            })
        } catch(e: DatabaseOperationException) {
            emit(
                Resource.Error<List<ProductDetailDto>>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        }
    }
}