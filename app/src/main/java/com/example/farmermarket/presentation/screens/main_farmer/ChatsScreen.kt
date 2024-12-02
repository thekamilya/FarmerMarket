package com.example.farmermarket.presentation.screens.main_farmer

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.farmermarket.R
import com.example.farmermarket.common.Constants
import com.example.farmermarket.data.remote.dto.Conversation
import com.example.farmermarket.data.remote.dto.Participants
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatsScreen(navController: NavController, viewModel: FarmerViewModel){

    LaunchedEffect(Unit) {
        viewModel.getChats(Constants.uuid)

    }

//    Button(onClick = {
//
//        val participant = "e70c818b-2453-4c3b-8133-7b0b57005bb1"
//        viewModel.createNewChat(participant) {
//            viewModel.selectedParticipantName.value = participant
//            viewModel.selectedChatId.value = it.id
//            viewModel.getChat(it, Constants.uuid)
//
//            navController.navigate(FarmerScreens.CHAT.name)
//        }
//    }) {
//
//
//    }



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
                val participant = getParticipantName(
                    participants = chat.participants,
                    userId = Constants.uuid
                )
                ChatListItem(conversation = chat, userId = Constants.uuid) { id->
                    Log.i("kama", "Other participant: $participant")

                    if (chat.lastMessage.senderId != Constants.uuid && !chat.lastMessage.readStatus){
                        viewModel.markAsRead(chat.id)
                    }
                    viewModel.selectedParticipantName.value = participant
                    viewModel.selectedChatId.value = id
                    viewModel.getChat(chat, Constants.uuid)
                    navController.navigate(FarmerScreens.CHAT.name)
                }
            }
        }

    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListItem(
    conversation: Conversation,
    userId: String,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    Row(
        modifier = modifier
            .height(92.dp)
            .fillMaxWidth()
            .clickable(onClick = { onClick(conversation.id,) })
            .background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        // Example placeholder profile picture (you can replace it with participants' profile pics)
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.avatarimage), // Replace with actual image URL
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
            ,
            contentScale = ContentScale.Crop

        )

        Spacer(modifier = Modifier.width(15.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Participants

            Row( modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Column {

                    Text(
                        text = "${getParticipantName(conversation.participants, userId)}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    // Last message
                    Text(
                        text = "${conversation.lastMessage.text}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 12.sp,
                        maxLines = 1,
                        color = if (conversation.lastMessage.senderId != Constants.uuid && !conversation.lastMessage.readStatus)
                            { Color(0xFF4CAD73)}
                        else{
                            Color.Black},

                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Column (horizontalAlignment = Alignment.End){
                    Text(
                        text = formatTimestamp(conversation.lastMessage.timestamp),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    if (conversation.lastMessage.senderId != Constants.uuid && !conversation.lastMessage.readStatus){
                        Box(
                            modifier = Modifier
                                .size(24.dp) // Size of the circle
                                .clip(CircleShape)
                                .background(Color(0xFF4CAD73)), // Circle color
                            contentAlignment = Alignment.Center // Center the text inside the circle
                        ) {
                            Text(
                                text = "1", // The number to display
                                color = Color.White, // Text color
                                fontSize = 14.sp, // Adjust font size
                                fontWeight = FontWeight.Bold // Bold text
                            )
                        }
                    }
                }




            }
        }



        // Timestamp (you can format the timestamp as needed)

    }
}


fun getParticipantName(participants: Map<Participants, Boolean>, userId: String): String {
    val otherParticipant = participants.keys.firstOrNull { it.userId != userId }


    // Return a placeholder or perform a lookup for the actual name based on the userId
    return otherParticipant?.userName ?: "Unknown"
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimestamp(timestamp: Long): String {
    // Convert the timestamp to an Instant
    val instant = Instant.ofEpochSecond(timestamp)



    // Format the Instant to a human-readable string using a DateTimeFormatter
    val formatter1 = DateTimeFormatter.ofPattern("HH:mm")
        .withZone(ZoneId.of("UTC"))

    val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .withZone(ZoneId.of("UTC"))
    Log.i("KAMA", (System.currentTimeMillis() / 1000).toString() + "   " + formatter2.format(instant))




    if ((System.currentTimeMillis() / 1000).toString()  == formatter2.format(instant)){

        return formatter1.format(instant)
    } else{
        return formatter2.format(instant)
    }


//    return formatter.format(instant)
}