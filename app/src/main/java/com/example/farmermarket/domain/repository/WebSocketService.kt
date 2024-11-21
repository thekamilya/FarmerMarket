package com.example.farmermarket.domain.repository

interface WebSocketService {

    suspend fun connect(onMessageReceived:(String) -> Unit)

//    fun sendMessage(message: String)
}