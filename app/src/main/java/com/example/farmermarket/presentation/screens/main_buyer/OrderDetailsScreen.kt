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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.farmermarket.R
import com.example.farmermarket.data.remote.dto.ProductDetailDto

@Composable
fun OrderDetailsScreen(navController: NavController, viewModel: BuyerViewModel) {


    val order = viewModel.selectedOrder.value

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
                Text(text = "Order Id: " + order.id.toString(), fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(7.dp))
//                Spacer(modifier = Modifier.height(14.dp))
//                Row(verticalAlignment = Alignment.Bottom) {
//                    Text(
//                        text = product.price.toString(),
//                        fontSize = 24.sp,
//                        fontWeight = FontWeight.SemiBold,
//                        color = Color(0xFF4CAD73)
//                    )
//                    Text(text = "/" + product.unit, fontSize = 18.sp)
//
//                }
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
//                Spacer(modifier = Modifier.height(7.dp))
//
//                Text(text = "Amount: ${offer.amount}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))
//
//                Spacer(modifier = Modifier.height(15.dp))
//
//                Text(text = "Order price: ${offer.orderPrice} KZT", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))
//
//                Spacer(modifier = Modifier.height(15.dp))
//
//                Text(text = "Offer price: ${offer.offeredPrice} KZT", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))
//
//                Spacer(modifier = Modifier.height(15.dp))
//
//                Text(text = "Message: ${offer.message} ", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))
//
//                Spacer(modifier = Modifier.height(15.dp))
//
//                Text(text = "Status:", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))
//
//                Spacer(modifier = Modifier.height(15.dp))

                for ((index, product) in order.products.withIndex()){

                    Spacer(modifier = Modifier.height(7.dp))

                    Text(text = "Product ${index+1}" , fontSize = 16.sp,  fontWeight = FontWeight.Medium, color = Color.Black)

                    Spacer(modifier = Modifier.height(7.dp))

                    Text(text = "Name: ${product.name}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(text = "Sold price: ${product.soldPrice}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(text = "Quantity: ${product.quantity}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(text = "Status:", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

                    Spacer(modifier = Modifier.height(25.dp))

                    val drawableId = when (product.status) {
                        "ORDERED" -> R.drawable.status_ordered
                        "IN TRANSIT" -> R.drawable.status_intransit
                        "PACKED" -> R.drawable.status_packed
                        "COMPLETED" -> R.drawable.status_delivered
                        "CANCELLED" -> R.drawable.status_cancelled
                        else -> R.drawable.status_cancelled
                    }

                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(25.dp))

                    Spacer(
                        modifier = Modifier
                            .background(Color.Gray)
                            .fillMaxWidth()
                            .height((0.5).dp)
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))

                //profile block
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
                            text = "Timmy Farmer",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    }


                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.chats_nav),
                        contentDescription = "",
                        tint = Color(0xFFBCBEBC),
                        modifier = Modifier.clickable {

                        })

                }

            }
        }


    }

}