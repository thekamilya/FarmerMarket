package com.example.farmermarket.presentation.screens.main_buyer



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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.farmermarket.R


@Composable
fun OfferDetailsScreen(navController: NavController, viewModel: BuyerViewModel) {



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
                }, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

                Spacer(modifier = Modifier.height(15.dp))



            }
        }


    }

}