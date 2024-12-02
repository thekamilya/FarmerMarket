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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.farmermarket.R
import com.example.farmermarket.common.Constants
import com.example.farmermarket.presentation.screens.main_farmer.FarmerViewModel
import com.example.farmermarket.presentation.screens.main_farmer.StatisticalElement
import com.example.farmermarket.presentation.screens.main_farmer.StatisticalElement2

@Composable
fun ProfileScreen(navController: NavController, viewModel: BuyerViewModel, onLogOut: ()-> Unit) {


    LaunchedEffect(Unit) {
        viewModel.getBuyerDashBoard()
    }


    Image(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenHeightDp.dp) / 2.5f),
        painter = painterResource(id = R.drawable.frame_217),
        contentDescription = null, // or provide a meaningful description
        contentScale = ContentScale.Crop,
        alignment = Alignment.BottomCenter

    )
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier.padding(top = 50.dp, start = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.avatarimage),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop

            )

            Spacer(modifier = Modifier.width(24.dp))

            Column() {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = Constants.userName + " " + Constants.surname,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = Constants.phone, fontSize = 16.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = Constants.email, fontSize = 16.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))


        //I want a menu btton at the top right of the screen and when I click it the box below appears

        val buyerDashBoard = viewModel.buyerDashBoard.value

        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(19.dp)
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(18.dp)
            ) {


                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Expenditure: ",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = buyerDashBoard.expenditure.toString() + " kzt",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(19.dp)
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(18.dp)
            ) {


                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total Purchases: ", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                    Text(
                        text = buyerDashBoard.totalBoughts.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(19.dp)
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(18.dp)
            ) {


                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Product Quantities: ",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(
                    modifier = Modifier
                        .background(Color(0xffEFEFEF))
                        .fillMaxWidth()
                        .height((0.5).dp)
                )
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.weight(30f),
                        text = "Name ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xffBCBEBC)
                    ) //1/3
                    Text(
                        modifier = Modifier.weight(50f),
                        text = "Popularity ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xffBCBEBC)
                    )
                    Text(
                        modifier = Modifier.weight(20f),
                        textAlign = TextAlign.End,
                        text = "Quantity % ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xffBCBEBC)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .background(Color(0xffEFEFEF))
                        .fillMaxWidth()
                        .height((0.5).dp)
                )
                for (totalQuantity in buyerDashBoard.productQuantities) {

                    StatisticalElement2(
                        name = totalQuantity.key,
                        totalQuantity = buyerDashBoard.totalQuantity,
                        productQuantity = totalQuantity.value
                    )
                    Spacer(
                        modifier = Modifier
                            .background(Color(0xffEFEFEF))
                            .fillMaxWidth()
                            .height((0.5).dp)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
            }

        }

        Spacer(modifier = Modifier.height(100.dp))

    }

    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 50.dp, end = 24.dp),
            onClick = { showMenu = !showMenu },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.White, // Set icon color to white
                containerColor = Color.Transparent // Set background color
            )
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
        }
        if (showMenu) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 80.dp, end = 50.dp)
                    .width(250.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(19.dp)
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(18.dp)
                ) {


                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "Profile", fontSize = 14.sp)
                    }
                    Spacer(
                        modifier = Modifier
                            .background(Color(0xffEFEFEF))
                            .fillMaxWidth()
                            .height((0.5).dp)
                    )

                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "Farm Details", fontSize = 14.sp)
                    }
                    Spacer(
                        modifier = Modifier
                            .background(Color(0xffEFEFEF))
                            .fillMaxWidth()
                            .height((0.5).dp)
                    )

                    Spacer(
                        modifier = Modifier
                            .background(Color(0xffEFEFEF))
                            .fillMaxWidth()
                            .height((0.5).dp)
                    )

                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth().clickable {

                                onLogOut()
                            }
                    ) {
                        Text(text = "Logout", fontSize = 14.sp, color = Color(0xff4CAD73))
                    }

                }
            }
        }
    }
}