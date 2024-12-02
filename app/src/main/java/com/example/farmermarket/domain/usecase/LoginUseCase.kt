package com.example.farmermarket.domain.usecase

import android.util.Log
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.LoginDto
import com.example.farmermarket.data.remote.dto.LoginResponseDto
import com.example.farmermarket.data.remote.dto.SignupDto
import com.example.farmermarket.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(loginDto: LoginDto): Flow<Resource<LoginResponseDto>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.logIn(loginDto)
            emit(Resource.Success(response))
            Log.d("kama", response.toString())
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
            Log.d("kama", "An unexpected error occured"+ e.message.toString())
        } catch (e: IOException) {
            Log.d("kama", "An unexpected error occuredd"+ e.message.toString())
            emit(Resource.Error(e.message.toString()))
        }
    }
}