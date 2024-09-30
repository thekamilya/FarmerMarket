package com.example.farmermarket.domain.usecase

import android.util.Log
import com.example.farmermarket.data.remote.dto.Conversation
import com.example.farmermarket.data.repository.FirebaseRepository


class GetChatsUseCase {
    operator fun invoke(user: String, onChatRoomsRetrieved: (List<Conversation>) -> Unit){
        val repo = FirebaseRepository()
        repo.getChatsForUser(user) { chatRooms ->

            onChatRoomsRetrieved(chatRooms)
        }

    }
}