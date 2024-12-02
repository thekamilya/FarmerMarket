package com.example.farmermarket.domain.usecase

import com.example.farmermarket.data.repository.FirebaseRepository

class AddUserToFirebaseUseCase {
    operator fun invoke(userId: String, userName: String){
        val repo = FirebaseRepository()
        repo.addUserToFirebase(userId, userName)

    }
}