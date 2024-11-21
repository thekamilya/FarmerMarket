package com.example.farmermarket.domain.usecase

import android.util.Log
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.ProductResponseDto
import com.example.farmermarket.domain.repository.FarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: FarmRepository
) {
    operator fun invoke(id:Int): Flow<Resource<Response<ResponseBody>>> = flow {
        try {
            emit(Resource.Loading())
            val response  = repository.deleteProduct(id)
            emit(Resource.Success(response))

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
            Log.d("kama", e.message.toString())
        } catch (e: IOException) {
            Log.d("kama", e.message.toString())
            emit(Resource.Error(e.message.toString()))
        }
    }
}