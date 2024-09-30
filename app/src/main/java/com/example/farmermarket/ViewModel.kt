package com.example.farmermarket

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.Conversation
import com.example.farmermarket.data.remote.dto.Item
import com.example.farmermarket.data.remote.dto.Message
import com.example.farmermarket.domain.usecase.GetBookUseCase
import com.example.farmermarket.domain.usecase.GetChatUseCase
import com.example.farmermarket.domain.usecase.GetChatsUseCase
import com.example.farmermarket.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

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
//
//@HiltViewModel
//class ViewModel @Inject constructor(
//    private val getBooksUseCase: GetBookUseCase,
//
//    ) : ViewModel() {
//
//    private val _listState = mutableStateOf(emptyList<Item>())
//    val listState: State<List<Item>> = _listState
//
//
//    fun getBooks(){
//        getBooksUseCase().onEach { result ->
//            when (result) {
//                is Resource.Success -> {
//                    _listState.value = result.data!!
//                    Log.d("KAMA", result.data.toString())
//                    Log.d("KAMA", "success")
//
//
//                }
//                is Resource.Error -> {
////                    _listState.value = BooksListState(
////                        error = result.message ?: "An unexpected error occurred"
////                    )
//                    Log.d("KAMA", result.message.toString())
//
//                }
//                is Resource.Loading -> {
////                    _listState.value = BooksListState(isLoading = true)
//                    Log.d("KAMA", "loading")
//
//                }
//            }
//        }.launchIn(viewModelScope)
//
//    }
//}