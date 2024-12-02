package com.example.farmermarket.domain.usecase

import android.util.Log
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.Conversation
import com.example.farmermarket.data.repository.DatabaseOperationException
import com.example.farmermarket.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException


class GetChatsUseCase {
    operator fun invoke(user: String, onChatRoomsRetrieved: (List<Conversation>) -> Unit): Flow<Resource<List<Conversation>>> = flow {
        try {
            emit(Resource.Loading())
            val repo = FirebaseRepository()
            var chatList = emptyList<Conversation>()
            repo.getChatsForUser(user) { chatRooms ->
                chatList = chatRooms
                onChatRoomsRetrieved(chatRooms)
            }
        } catch(e: DatabaseOperationException) {
            emit(
                Resource.Error<List<Conversation>>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        }
    }
}