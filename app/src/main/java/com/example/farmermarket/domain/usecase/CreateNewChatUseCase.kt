package com.example.farmermarket.domain.usecase

import com.example.farmermarket.data.repository.FirebaseRepository


class CreateNewChatUseCase {
    operator fun invoke(user: String, otherParticipant:String){
        val repo = FirebaseRepository()
//        repo.createChat(user, otherParticipant)

    }
}