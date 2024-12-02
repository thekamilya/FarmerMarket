package com.example.farmermarket.domain.usecase

import com.example.farmermarket.data.remote.dto.Message
import com.example.farmermarket.data.repository.FirebaseRepository

class GetUsernameByUserIdUseCase {
    operator fun invoke(userId: String, onSuccess: (String) -> Unit){
        val repo = FirebaseRepository()
        repo.getUserNameById(userId, onSuccess, {})
    }
}