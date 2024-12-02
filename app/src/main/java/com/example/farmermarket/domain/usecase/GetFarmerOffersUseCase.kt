package com.example.farmermarket.domain.usecase

import android.util.Log
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.OffersListDto
import com.example.farmermarket.domain.repository.FarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetFarmerOffersUseCase @Inject constructor(
    private val repository: FarmRepository
) {
    operator fun invoke(): Flow<Resource<OffersListDto>> = flow {
        try {
            emit(Resource.Loading())
            val offers = repository.getFarmerOffers()
            emit(Resource.Success<OffersListDto>(offers))
            Log.d("kama", offers.toString())
        } catch (e: HttpException) {
            emit(Resource.Error<OffersListDto>(e.localizedMessage ?: "An unexpected error occured"))
            Log.d("kama", e.message.toString())
        } catch (e: IOException) {
            Log.d("kama", e.message.toString())
            emit(Resource.Error<OffersListDto>(e.message.toString()))
        }
    }
}