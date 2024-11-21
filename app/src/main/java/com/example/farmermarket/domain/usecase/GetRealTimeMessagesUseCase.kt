package com.example.farmermarket.domain.usecase

import com.example.farmermarket.data.remote.dto.Message
import com.example.farmermarket.data.repository.FirebaseRepository
import com.example.farmermarket.domain.repository.WebSocketService

class GetRealTimeMessagesUseCase(private val webSocketService: WebSocketService) {

    suspend operator fun invoke(onMessageReceived: (String) -> Unit) {
        webSocketService.connect(onMessageReceived)
    }
}