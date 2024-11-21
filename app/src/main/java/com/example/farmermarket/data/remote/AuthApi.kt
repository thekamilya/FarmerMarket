package com.example.farmermarket.data.remote

import com.example.farmermarket.data.remote.dto.LoginDto
import com.example.farmermarket.data.remote.dto.LoginResponseDto
import com.example.farmermarket.data.remote.dto.ProductDTO
import com.example.farmermarket.data.remote.dto.SignupDto
import com.example.farmermarket.domain.usecase.LoginUseCase
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface AuthApi {


    @POST("/user/signup")
    suspend fun signUp(@Body signupDto: SignupDto): Response<ResponseBody>

    @POST("/user/login")
    suspend fun login(@Body loginDto: LoginDto): LoginResponseDto
}