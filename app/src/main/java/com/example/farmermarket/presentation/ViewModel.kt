package com.example.farmermarket.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.farmermarket.data.remote.dto.Conversation
import com.example.farmermarket.data.remote.dto.Message
import com.example.farmermarket.domain.usecase.GetChatUseCase
import com.example.farmermarket.domain.usecase.GetChatsUseCase
import com.example.farmermarket.domain.usecase.SendMessageUseCase


class MyViewModel(
    private val getChatsUseCase: GetChatsUseCase = GetChatsUseCase(),
    private val getChatUseCase: GetChatUseCase = GetChatUseCase(),
    private val sendMessageUseCase: SendMessageUseCase = SendMessageUseCase()
): ViewModel() {


    val chatList: MutableState<List<Conversation>> =  mutableStateOf(emptyList<Conversation>())
    val messageList: MutableState<List<Message>> = mutableStateOf(emptyList())

    val selectedChatId: MutableState<String> = mutableStateOf("")
    val selectedParticipantName: MutableState<String> = mutableStateOf("")

    fun getChats(user: String){

        getChatsUseCase(user) { chatRooms ->

            chatList.value = chatRooms
        }
    }

    fun getChat(conversationId: String){

        getChatUseCase(conversationId){messages->
            messageList.value = messages
        }
    }

    fun sendMessage(conversationId: String, message: String, userName: String){
        sendMessageUseCase(conversationId, message, userName)

    }
}