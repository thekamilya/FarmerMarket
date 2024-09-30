package com.example.testproj.presentation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.farmermarket.R
import com.example.farmermarket.common.Constants.userName
import com.example.farmermarket.data.remote.dto.Conversation
import com.example.farmermarket.presentation.MyViewModel
import com.example.farmermarket.presentation.Screens


@Composable
fun ChatListScreen( navController: NavHostController, viewModel: MyViewModel){

//    LaunchedEffect(Unit) {
//        viewModel.getChats("jong")
//
//    }
    val chatList = viewModel.chatList
    Log.i("kama", "this is chatlist" + chatList.value.toString())

    LazyColumn(){
        item{
            Text(text = "ChatList")
        }
        items(chatList.value){chat->
            viewModel.selectedParticipantName.value = getParticipantName(
                participants = chat.participants,
                userName = "kamila"
            )
            ChatListItem(conversation = chat, userName = userName) {id->
                viewModel.selectedChatId.value = id
                navController.navigate(Screens.CHAT.name)
                viewModel.getChat(id)
            }
        }
    }
}

@Composable
fun ChatListItem(
    conversation: Conversation,
    userName: String,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    Row(
        modifier = modifier
            .height(60.dp)
            .fillMaxWidth()
            .clickable(onClick = { onClick(conversation.id,) })
            .background(color = Color.LightGray),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        // Example placeholder profile picture (you can replace it with participants' profile pics)
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.ic_launcher_foreground), // Replace with actual image URL
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                ,
            contentScale = ContentScale.Crop

        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Participants
            Text(
                text = "${getParticipantName(conversation.participants, userName)}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Last message
            Text(
                text = "${conversation.lastMessage.text}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Timestamp (you can format the timestamp as needed)
        Text(
            text = formatTimestamp(conversation.lastMessage.timestamp),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Composable
fun getParticipantName(participants: Map<String, Boolean>, userName: String): String {
    val otherParticipant = participants.keys.firstOrNull { it != userName }
    Log.i("PART", "Other participant: $otherParticipant")

    // Return a placeholder or perform a lookup for the actual name based on the userId
    return otherParticipant ?: "Unknown"
}

fun formatTimestamp(timestamp: Long): String {
    // Example timestamp formatting (you can customize this)
    return "12:34 PM" // Placeholder, use actual formatting logic
}