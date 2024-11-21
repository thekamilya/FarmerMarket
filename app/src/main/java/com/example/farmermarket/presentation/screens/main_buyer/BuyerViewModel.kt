package com.example.farmermarket.presentation.screens.main_buyer

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.Message
import com.example.farmermarket.domain.usecase.GetChatUseCase
import com.example.farmermarket.domain.usecase.GetChatsUseCase
import com.example.farmermarket.domain.usecase.GetRealTimeMessagesUseCase
import com.example.farmermarket.domain.usecase.SendMessageUseCase
import com.example.farmermarket.presentation.screens.main_farmer.ChatListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BuyerViewModel  @Inject constructor(

    private val getChatsUseCase: GetChatsUseCase,
    private val getChatUseCase: GetChatUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getRealTimeMessagesUseCase: GetRealTimeMessagesUseCase
): ViewModel() {


    val chatListState: MutableState<ChatListState> = mutableStateOf(ChatListState())
    val messageList: MutableState<List<Message>> = mutableStateOf(emptyList())
    val selectedChatId: MutableState<String> = mutableStateOf("")
    val selectedParticipantName: MutableState<String> = mutableStateOf("")


    init {
        viewModelScope.launch {
            getRealTimeMessagesUseCase { message ->
                Log.i("SOCKET", message)

            }
        }
    }



    fun getChats(user: String) {

        getChatsUseCase(user) { chatRooms ->

            chatListState.value = ChatListState(chatResponse = chatRooms)
        }.onEach { result ->
            when (result) {
                is Resource.Success -> {
                    chatListState.value =
                        result.data?.let { ChatListState(chatResponse = it) }!!
                    Log.d("LAB", result.data.toString())
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

    fun getChat(conversationId: String) {

        getChatUseCase(conversationId) { messages ->
            messageList.value = messages
            Log.i("MESSAGE", messages.toString())
        }
    }

    fun sendMessage(conversationId: String, message: String, userName: String) {
        sendMessageUseCase(conversationId, message, userName)

    }
}

