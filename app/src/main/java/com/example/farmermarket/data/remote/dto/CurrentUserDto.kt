package com.example.farmermarket.data.remote.dto

data class CurrentUserDto(
    val created_at: String,
    val deleted_at: Any,
    val email: String,
    val first_name: String,
    val last_name: String,
    val middle_name: String,
    val phone: String,
    val role: String,
    val updated_at: Any,
    val uuid: String
)