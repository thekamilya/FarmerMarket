package com.example.farmermarket.domain.usecase

import com.example.farmermarket.data.remote.dto.Message
import com.example.farmermarket.data.repository.FirebaseRepository


class GetChatUseCase {
    operator fun invoke(conversationId: String, onConversationRetrieved: (List<Message>) -> Unit){
        val repo = FirebaseRepository()
        repo.getChat(conversationId, onConversationRetrieved)
    }
}