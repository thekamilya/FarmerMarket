package com.example.farmermarket.presentation.screens.main_farmer

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.farmermarket.R
import com.example.farmermarket.common.Constants
import com.example.farmermarket.data.remote.dto.Message

import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController, viewModel: FarmerViewModel){


    Column(modifier = Modifier.background(Color(0xFFF4F4F4))) {

        ChatHeader(viewModel.selectedParticipantName.value, onBackClicked = {navController.popBackStack()})
        val messagesList = viewModel.messageList
        Log.i("MESSAGE1", viewModel.messageList.toString())

        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(messagesList.value.size) {
            // Scroll to the last item when the size of the list changes
            if (messagesList.value.isNotEmpty()) {
                coroutineScope.launch {
                    listState.animateScrollToItem(messagesList.value.size - 1)
                }
            }
        }
        var date = " "
        LazyColumn(state = listState,
            modifier = Modifier
                .fillMaxHeight(0.9F)){
            items(messagesList.value){message->

                val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .withZone(ZoneId.of("UTC"))
               
                if (date != formatter2.format(Instant.ofEpochSecond(message.timestamp))){
                    date = formatter2.format(Instant.ofEpochSecond(message.timestamp))
                    
                    Text(text = date,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 12.sp,)
                }
                
                
                MessageElement(message = message)
            }
        }
        var message by remember { mutableStateOf("") }  // State to hold the message text

        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(20.dp)
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Input field for typing message
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,),
                    shape = RoundedCornerShape(16.dp),
                    value = message,

                    onValueChange = { message = it },  // Update the message state
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    placeholder = { Text("Type a message") }
                )

                // Send button
                IconButton(

                    onClick = {
                        // Logic to send the message
                        if (message.isNotEmpty()) {

                            // Call your send message function here
                            viewModel.sendMessage(viewModel.selectedChatId.value,message,
                                Constants.uuid
                            )
                            message = ""
                        }
                    },
                    enabled = message.isNotEmpty()  // Disable the button when message is empty
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.send_icon),
                        tint = Color(0xff4CAD73),// You can replace this with your own icon
                        contentDescription = "Send Message"
                    )

                }
            }

        }
    }


}

@Composable
fun ChatHeader(participantName: String, onBackClicked:()-> Unit){
    Column(modifier = Modifier
//        .height(74.dp)
        .background(Color.White)) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(10.dp))

            IconButton(
                onClick = {
                    onBackClicked()
                },
            ) {
                Icon(
                    modifier = Modifier.size(15.dp),
                    imageVector = Icons.Default.ArrowBackIosNew,
                    tint = Color(0XFF828282),// You can replace this with your own icon
                    contentDescription = "Send Message"
                )

            }

            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.avatarimage), // Replace with actual image URL
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop

            )
            Spacer(modifier = Modifier.width(15.dp))

            Text(
                text = participantName,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageElement(message: Message) {

    val formatter1 = DateTimeFormatter.ofPattern("HH:mm")
        .withZone(ZoneId.of("UTC"))

    val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .withZone(ZoneId.of("UTC"))

    val formattedTime = formatter1.format(Instant.ofEpochSecond(message.timestamp))
    val formattedDate = formatter2.format(Instant.ofEpochSecond(message.timestamp))

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = if (message.senderId == Constants.uuid) Alignment.End else Alignment.Start
    ) {
        // Display date only once per day
//        Text(
//            text = formattedDate,
//            color = Color.Gray,
//            fontSize = 12.sp,
//            modifier = Modifier.padding(bottom = 4.dp)
//        )
        Row(
            horizontalArrangement = if (message.senderId == Constants.uuid) Arrangement.End else Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (message.senderId == Constants.uuid) 16.dp else 4.dp,
                            bottomEnd = if (message.senderId == Constants.uuid) 4.dp else 16.dp
                        )
                    )
                    .background(
                        color = if (message.senderId == Constants.uuid) Color(0xff4CAD73) else Color(
                            0xffECEAEA
                        )
                    )
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier
                        .padding(15.dp)
                        .widthIn(max = (0.7f * LocalConfiguration.current.screenWidthDp).dp),
                    color = if (message.senderId == Constants.uuid) Color.White else Color.Black
                )
                Text(
                    text = formattedTime,
                    color = if (message.senderId == Constants.uuid) Color.LightGray else Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                )
            }
        }
    }
}