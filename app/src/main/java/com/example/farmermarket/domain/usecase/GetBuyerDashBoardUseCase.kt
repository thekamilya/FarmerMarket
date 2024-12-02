package com.example.farmermarket.domain.usecase

import android.util.Log
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.BuyerDashBoardDto
import com.example.farmermarket.data.remote.dto.FarmerDashBoardDto
import com.example.farmermarket.domain.repository.FarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetBuyerDashBoardUseCase @Inject constructor(
    private val repository: FarmRepository
) {
    operator fun invoke(): Flow<Resource<BuyerDashBoardDto>> = flow {
        try {
            emit(Resource.Loading())
            val dashBoardDto = repository.getBuyerDashBoard()
            emit(Resource.Success<BuyerDashBoardDto>(dashBoardDto))
            Log.d("kama", dashBoardDto.toString())
        } catch (e: HttpException) {
            emit(Resource.Error<BuyerDashBoardDto>(e.localizedMessage ?: "An unexpected error occured"))
            Log.d("kama", e.message.toString())
        } catch (e: IOException) {
            Log.d("kama", e.message.toString())
            emit(Resource.Error<BuyerDashBoardDto>(e.message.toString()))
        }
    }
}