package com.example.farmermarket.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SignupDto(
    val password: String,
    val phone: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val middle_name: String
)