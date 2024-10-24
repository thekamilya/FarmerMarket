package com.example.farmermarket.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.Conversation
import com.example.farmermarket.data.remote.dto.Message
import com.example.farmermarket.domain.usecase.GetChatUseCase
import com.example.farmermarket.domain.usecase.GetChatsUseCase
import com.example.farmermarket.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


data class ChatListState(
    val isLoading: Boolean = false,
    val chatResponse: List<Conversation> =  emptyList(),
    val error: String = ""
)

class MyViewModel(
    private val getChatsUseCase: GetChatsUseCase = GetChatsUseCase(),
    private val getChatUseCase: GetChatUseCase = GetChatUseCase(),
    private val sendMessageUseCase: SendMessageUseCase = SendMessageUseCase()
): ViewModel() {


    val chatListState: MutableState<ChatListState> =  mutableStateOf(ChatListState())
    val messageList: MutableState<List<Message>> = mutableStateOf(emptyList())

    val selectedChatId: MutableState<String> = mutableStateOf("")
    val selectedParticipantName: MutableState<String> = mutableStateOf("")

    fun getChats(user: String){

        getChatsUseCase(user) { chatRooms ->

            chatListState.value = ChatListState(chatResponse = chatRooms)
        }.onEach { result ->
            when (result) {
                is Resource.Success -> {
                    chatListState.value = result.data?.let { ChatListState(chatResponse = it) }!!
                }
                is Resource.Error -> {
                    chatListState.value = ChatListState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    Log.d("LAB", "error")

                }
                is Resource.Loading -> {
                    chatListState.value = ChatListState(isLoading = true)
                    Log.d("LAB", "loading")

                }
            }
        }.launchIn(viewModelScope)



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