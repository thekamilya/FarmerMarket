package com.example.farmermarket.domain.repository


import com.example.farmermarket.data.remote.dto.LoginDto
import com.example.farmermarket.data.remote.dto.LoginResponseDto
import com.example.farmermarket.data.remote.dto.SignupDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

interface AuthRepository {

    suspend fun signUp(signupDto: SignupDto):  Response<ResponseBody>

    suspend fun logIn(loginDto: LoginDto):  LoginResponseDto
}