package com.example.farmermarket.presentation.screens.main_farmer

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatsScreen(navController: NavController, viewModel: FarmerViewModel){

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListItem(
    conversation: Conversation,
    userName: String,
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

            Row( modifier = Modifier.fillMaxWidth().padding(end = 20.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "${getParticipantName(conversation.participants, userName)}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = formatTimestamp(conversation.lastMessage.timestamp),
                    style = MaterialTheme.typography.bodySmall
                )



            }

            // Last message
            Text(
                text = "${conversation.lastMessage.text}",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Timestamp (you can format the timestamp as needed)

    }
}

@Composable
fun getParticipantName(participants: Map<String, Boolean>, userName: String): String {
    val otherParticipant = participants.keys.firstOrNull { it != userName }
    Log.i("PART", "Other participant: $otherParticipant")

    // Return a placeholder or perform a lookup for the actual name based on the userId
    return otherParticipant ?: "Unknown"
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