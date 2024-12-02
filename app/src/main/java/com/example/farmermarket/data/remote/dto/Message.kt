package com.example.farmermarket.data.remote.dto

data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val readStatus: Boolean = false)
