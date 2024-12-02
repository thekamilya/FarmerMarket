package com.example.farmermarket.domain.usecase

import com.example.farmermarket.data.repository.FirebaseRepository

class MarkAsReadUseCase {
    operator fun invoke(conversationId: String){
        val repo = FirebaseRepository()
        repo.markLastMessageAsRead(conversationId)

    }
}