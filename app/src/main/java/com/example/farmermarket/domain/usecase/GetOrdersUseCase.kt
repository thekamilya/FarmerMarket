package com.example.farmermarket.domain.usecase

import android.util.Log
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.OrdersResponseDto
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.domain.repository.FarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: FarmRepository
) {
    operator fun invoke(): Flow<Resource<OrdersResponseDto>> = flow {
        try {
            emit(Resource.Loading())
            val orders = repository.getOrders()
            emit(Resource.Success<OrdersResponseDto>(orders))
            Log.d("kama", orders.toString())
        } catch (e: HttpException) {
            emit(Resource.Error<OrdersResponseDto>(e.localizedMessage ?: "An unexpected error occured"))
            Log.d("kama", e.message.toString())
        } catch (e: IOException) {
            Log.d("kama", e.message.toString())
            emit(Resource.Error<OrdersResponseDto>(e.message.toString()))
        }
    }
}