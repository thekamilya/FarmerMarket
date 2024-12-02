package com.example.farmermarket.presentation.screens.main_farmer

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.farmermarket.R
import com.example.farmermarket.common.Constants
import com.example.farmermarket.common.Constants.uuid
import com.example.farmermarket.presentation.screens.main_buyer.BuyerViewModel


@Composable
fun OfferDetailsScreen(navController: NavController, viewModel: FarmerViewModel) {

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,){
        if(viewModel.changeOfferStatusState.value.isChanged== true){
            viewModel.changeOfferStatusState.value = ChangeOfferStatusState()
            Toast.makeText(LocalContext.current, "Updated!", Toast.LENGTH_SHORT).show()

        }else if(viewModel.changeOfferStatusState.value.error != ""){
            Toast.makeText(LocalContext.current, "Something went wrong! Try later", Toast.LENGTH_SHORT).show()
        }else if(viewModel.changeOfferStatusState.value.isLoading== true){
            CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

        }
    }


    val offer = viewModel.selectedOffer.value

    Box(modifier = Modifier.background(color = Color(0xff4CAD73))) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topEnd = 30.dp, topStart = 30.dp))
                .background(Color.White)
        ) {

            Column(modifier = Modifier
                .padding(24.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    navController.popBackStack()
                }
            ) {
                Column(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0x79000000)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }

            Column(Modifier.padding(24.dp)) {

                Spacer(modifier = Modifier.height(20.dp))
                Text(text = offer.product.name , fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(7.dp))
                Spacer(modifier = Modifier.height(14.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = offer.product.price.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4CAD73)
                    )
                    Text(text = "/" + offer.product.unit, fontSize = 18.sp)

                }
//                Spacer(modifier = Modifier.height(14.dp))

//                Row(verticalAlignment = Alignment.Bottom) {
//                    Text(
//                        text = "Products: ",
//                        fontSize = 14.sp,
//                        color = Color(0XFF828282)
//                    )
//                }


            }
            Spacer(
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .height((0.5).dp)
            )

            Column(Modifier.padding(24.dp)) {
                Spacer(modifier = Modifier.height(7.dp))

                Text(text = "Order price: ${offer.product.price} KZT", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

                Spacer(modifier = Modifier.height(15.dp))

                Text(text = "Offer price: ${offer.price} KZT", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

                Spacer(modifier = Modifier.height(15.dp))

                Text(text = "Message: ${offer.message} ", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

                Spacer(modifier = Modifier.height(15.dp))

                Text(text = when (offer.isAccepted) {
                    true -> "Status: Accepted"
                    false -> "Status: Rejected"
                    null -> "Status: Waiting for farmer response"
                    else -> {""}
                }, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD)
                )

                Spacer(modifier = Modifier.height(15.dp))

                Spacer(
                    modifier = Modifier
                        .background(Color.Gray)
                        .fillMaxWidth()
                        .height((0.5).dp)
                )

                Spacer(modifier = Modifier.height(100.dp))


                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color(0xFFF2F2F2))
                        .fillMaxWidth()
                        .height(67.dp)
                        .padding(horizontal = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberAsyncImagePainter(model = R.drawable.avatarimage),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop

                        )
                        Spacer(modifier = Modifier.width(15.dp))

                        Text(
                            text = viewModel.selectedOffer.value.buyerName.toString(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    }


                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.chats_nav),
                        contentDescription = "",
                        tint = Color(0xFFBCBEBC),
                        modifier = Modifier.clickable {

                            val participant = offer.userId
                            viewModel.createNewChat(participant) {
                                viewModel.selectedParticipantName.value = participant
                                viewModel.selectedChatId.value = it.id
                                viewModel.getChat(it, Constants.uuid)

                                navController.navigate(FarmerScreens.CHAT.name)
                            }

                        })

                }
                Spacer(modifier = Modifier.height(100.dp))
                Row() {
                    Button(
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f)  // Set the weight to equally divide the space
                            .fillMaxWidth()
                        ,
                        onClick = {
                            viewModel.changeOfferStatus(offer.id, false)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFFE44460)),


                        ) {
                        Text(text = "Reject", color = Color.White, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f)  // Set the weight to equally divide the space
                            .fillMaxWidth()
                        ,
                        onClick = {
                            viewModel.changeOfferStatus(offer.id, false)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFFFAB663))
                    ) {

                        Text(text = "Accept", color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}