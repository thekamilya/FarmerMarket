package com.example.farmermarket.data.repository

import com.example.farmermarket.data.remote.AuthApi
import com.example.farmermarket.data.remote.dto.LoginDto
import com.example.farmermarket.data.remote.dto.LoginResponseDto
import com.example.farmermarket.data.remote.dto.SignupDto
import com.example.farmermarket.domain.repository.AuthRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
): AuthRepository
{
    override suspend fun signUp(signupDto: SignupDto):  Response<ResponseBody> {
        return api.signUp(signupDto)
    }

    override suspend fun logIn(loginDto: LoginDto): LoginResponseDto {
        return api.login(loginDto)
    }

}