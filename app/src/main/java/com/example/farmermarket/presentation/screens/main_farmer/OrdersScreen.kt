package com.example.farmermarket.presentation.screens.main_farmer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farmermarket.data.remote.dto.SoldProductsDtoItem
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.presentation.screens.main_buyer.OfferCard



enum class OrdersScreens {
    OFFERS,
    NEW,
    PACKED,
    IN_TRANSIT,
    COMPLETED,
    CANCELLED
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdersScreen(navController: NavController, viewModel: FarmerViewModel){

    val showScreen = viewModel.showOrderScreen

    val scrollState = rememberScrollState()


    Column( horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Orders", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
                .height((0.5).dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                val interactionSource = remember { MutableInteractionSource() }
                Text(
                    text = "Offers",
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(30.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            viewModel.showOrderScreen.value = OrdersScreens.OFFERS
                        },
                    color = if (showScreen.value == OrdersScreens.OFFERS) {
                        Color(0xFF2CB56C)
                    } else {
                        Color(0xFFD0D1E3)
                    }
                )

                Text(
                    text = "New",
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(30.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            viewModel.showOrderScreen.value = OrdersScreens.NEW
                        },
                    color = if (showScreen.value == OrdersScreens.NEW) {
                        Color(0xFF2CB56C)
                    } else {
                        Color(0xFFD0D1E3)
                    }
                )

                Text(
                    text = "Packed",
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(30.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            viewModel.showOrderScreen.value = OrdersScreens.PACKED
                        },
                    color = if (showScreen.value == OrdersScreens.PACKED) {
                        Color(0xFF2CB56C)
                    } else {
                        Color(0xFFD0D1E3)
                    }
                )

                Text(
                    text = "In transit",
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(30.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            viewModel.showOrderScreen.value = OrdersScreens.IN_TRANSIT
                        },
                    color = if (showScreen.value == OrdersScreens.IN_TRANSIT) {
                        Color(0xFF2CB56C)
                    } else {
                        Color(0xFFD0D1E3)
                    }
                )

                Text(
                    text = "Completed",
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(30.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            viewModel.showOrderScreen.value = OrdersScreens.COMPLETED
                        },
                    color = if (showScreen.value == OrdersScreens.COMPLETED) {
                        Color(0xFF2CB56C)
                    } else {
                        Color(0xFFD0D1E3)
                    }
                )

                Text(
                    text = "Cancelled",
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(30.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            viewModel.showOrderScreen.value = OrdersScreens.CANCELLED
                        },
                    color = if (showScreen.value == OrdersScreens.CANCELLED) {
                        Color(0xFF2CB56C)
                    } else {
                        Color(0xFFD0D1E3)
                    }
                )

            }

            when (showScreen.value ){
                OrdersScreens.OFFERS ->{

                    LaunchedEffect(Unit) {
                        viewModel.getOffers()
                    }
                    val offers = viewModel.offerListState.value.data

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        items(offers) { offer ->

                            OfferCard(offer) {
                                viewModel.selectedOffer.value = offer
                                navController.navigate(FarmerScreens.OFFER_DETAILS.name)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item {
                            Spacer(modifier = Modifier.height(70.dp))
                        }

                    }

                }
                OrdersScreens.NEW ->{

                    LaunchedEffect(Unit) {
                        viewModel.getSoldProducts("ORDERED")
                    }
                    val orders = viewModel.orderedProductListState.value.orderedProductList


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item{
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        items(orders) { order ->

                            OrderCard(order) {
                                viewModel.selectedOrderDetail.value = order
                                navController.navigate(FarmerScreens.ORDER_DETAILS.name)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item{
                            Spacer(modifier = Modifier.height(70.dp))
                        }

                    }

                }
                OrdersScreens.IN_TRANSIT ->{

                    LaunchedEffect(Unit) {
                        viewModel.getSoldProducts("IN TRANSIT")
                    }
                    val orders = viewModel.inTransitProductListState.value.inTransitProductList


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item{
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        items(orders) { order ->

                            OrderCard(order) {
                                viewModel.selectedOrderDetail.value = order
                                navController.navigate(FarmerScreens.ORDER_DETAILS.name)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item{
                            Spacer(modifier = Modifier.height(70.dp))
                        }

                    }

                }

                OrdersScreens.PACKED ->{

                    LaunchedEffect(Unit) {
                        viewModel.getSoldProducts("PACKED")
                    }
                    val orders = viewModel.packedProductListState.value.packedProductList


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item{
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        items(orders) { order ->

                            OrderCard(order) {
                                viewModel.selectedOrderDetail.value = order
                                navController.navigate(FarmerScreens.ORDER_DETAILS.name)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item{
                            Spacer(modifier = Modifier.height(70.dp))
                        }

                    }

                }

                OrdersScreens.COMPLETED ->{

                    LaunchedEffect(Unit) {
                        viewModel.getSoldProducts("COMPLETED")
                    }
                    val orders = viewModel.completedProductListState.value.completedProductList


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item{
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        items(orders) { order ->

                            OrderCard(order) {
                                viewModel.selectedOrderDetail.value = order
                                navController.navigate(FarmerScreens.ORDER_DETAILS.name)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item{
                            Spacer(modifier = Modifier.height(70.dp))
                        }

                    }

                }

                OrdersScreens.CANCELLED ->{

                    LaunchedEffect(Unit) {
                        viewModel.getSoldProducts("CANCELLED")
                    }
                    val orders = viewModel.cancelledProductListState.value.cancelledProductList


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item{
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        items(orders) { order ->

                            OrderCard(order) {
                                viewModel.selectedOrderDetail.value = order
                                navController.navigate(FarmerScreens.ORDER_DETAILS.name)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item{
                            Spacer(modifier = Modifier.height(70.dp))
                        }

                    }

                }

                else -> {



                }
            }






        }

    }

//        CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

    when (showScreen.value){
        OrdersScreens.OFFERS ->{
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,){
                if(viewModel.offerListState.value.isLoading){
                    CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

                }else if(viewModel.offerListState.value.error != ""){
                    Text(text = "Network error")

                }else if (viewModel.offerListState.value.data.isEmpty() && !viewModel.offerListState.value.isLoading){
                    Text(text = "This page is empty")
                }
            }
        }

        OrdersScreens.NEW ->{
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,){
                if(viewModel.orderedProductListState.value.isLoading == true){
                    CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

                }else if(viewModel.orderedProductListState.value.error != ""){
                    Text(text = "Network error")

                }else if (viewModel.orderedProductListState.value.orderedProductList.isEmpty() && viewModel.orderedProductListState.value.isLoading == false){
                    Text(text = "This page is empty")
                }
            }
        }

        OrdersScreens.IN_TRANSIT ->{
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,){
                if(viewModel.inTransitProductListState.value.isLoading == true){
                    CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

                }else if(viewModel.inTransitProductListState.value.error != ""){
                    Text(text = "Network error")

                }else if (viewModel.inTransitProductListState.value.inTransitProductList.isEmpty() && viewModel.orderedProductListState.value.isLoading == false){
                    Text(text = "This page is empty")
                }
            }
        }

        OrdersScreens.PACKED ->{
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,){
                if(viewModel.packedProductListState.value.isLoading == true){
                    CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

                }else if(viewModel.packedProductListState.value.error != ""){
                    Text(text = "Network error")

                }else if (viewModel.packedProductListState.value.packedProductList.isEmpty() && viewModel.packedProductListState.value.isLoading == false){
                    Text(text = "This page is empty")
                }
            }
        }

        OrdersScreens.COMPLETED ->{
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,){
                if(viewModel.completedProductListState.value.isLoading == true){
                    CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

                }else if(viewModel.completedProductListState.value.error != ""){
                    Text(text = "Network error")

                }else if (viewModel.completedProductListState.value.completedProductList.isEmpty() && viewModel.orderedProductListState.value.isLoading == false){
                    Text(text = "This page is empty")
                }
            }
        }
        OrdersScreens.CANCELLED ->{
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,){
                if(viewModel.cancelledProductListState.value.isLoading == true){
                    CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

                }else if(viewModel.cancelledProductListState.value.error != ""){
                    Text(text = "Network error")

                }else if (viewModel.cancelledProductListState.value.cancelledProductList.isEmpty() && viewModel.orderedProductListState.value.isLoading == false){
                    Text(text = "This page is empty")
                }
            }
        }

        else -> {}
    }


}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderCard(soldProductDtoItem: SoldProductsDtoItem, onCardClick: () -> Unit){

    Box(modifier = Modifier
        .height(209.dp)
        .width(299.dp)
        .shadow(
            elevation = 10.dp,
            shape = RoundedCornerShape(10.dp),
            clip = true,
            spotColor = Color(0xFFBDBDBD)
        )
        .background(Color.White)
        .clip(RoundedCornerShape(10.dp))
        .background(Color.White)
        .clickable {
            onCardClick()
        }) {

        Column(Modifier.padding(24.dp)){

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(text =  "Name: " + soldProductDtoItem.name, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)

                Text(text = String.format("%.2f", soldProductDtoItem.soldPrice* soldProductDtoItem.quantity) + " kzt", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4CAD73))


            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "Status: ${soldProductDtoItem.status}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

            Spacer(modifier = Modifier.height(15.dp))

            Text(text = "Created date: ${soldProductDtoItem.soldDate}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

            Spacer(modifier = Modifier.height(15.dp))


        }



    }


}