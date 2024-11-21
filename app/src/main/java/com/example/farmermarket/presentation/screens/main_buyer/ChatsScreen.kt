package com.example.farmermarket.presentation.screens.main_buyer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farmermarket.common.Constants
import com.example.farmermarket.presentation.screens.main_farmer.ChatListItem
import com.example.farmermarket.presentation.screens.main_farmer.FarmerScreens
import com.example.farmermarket.presentation.screens.main_farmer.FarmerViewModel
import com.example.farmermarket.presentation.screens.main_farmer.getParticipantName

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatsScreen(navController: NavController, viewModel: BuyerViewModel){

    LaunchedEffect(Unit) {
        viewModel.getChats(Constants.userName)

    }



    val chatList = viewModel.chatListState.value.chatResponse

    if(viewModel.chatListState.value.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)
        }
    }else if (viewModel.chatListState.value.error != ""){
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "Network error")
        }
    }
    Column( horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Chats", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(modifier = Modifier
            .background(Color.Gray)
            .fillMaxWidth()
            .height((0.5).dp))

        LazyColumn(modifier = Modifier.fillMaxHeight()){

            items(chatList){chat->
                viewModel.selectedParticipantName.value = getParticipantName(
                    participants = chat.participants,
                    userName = "kamila"
                )
                ChatListItem(conversation = chat, userName = Constants.userName) { id->
                    viewModel.selectedChatId.value = id
                    viewModel.getChat(id)
                    navController.navigate(FarmerScreens.CHAT.name)
//                viewModel.getChat(id)
                }
            }
        }

    }


}