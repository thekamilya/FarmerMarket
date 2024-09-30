package com.example.farmermarket.domain.usecase

import android.util.Log
import com.example.farmermarket.data.repository.FirebaseRepository


class SendMessageUseCase {
    operator fun invoke(conversationId: String, message: String, userName: String){
        val repo = FirebaseRepository()
        repo.sendMessage(conversationId, message, userName)

    }
}