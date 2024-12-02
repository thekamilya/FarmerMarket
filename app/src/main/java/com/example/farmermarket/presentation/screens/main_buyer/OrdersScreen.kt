package com.example.farmermarket.presentation.screens.main_buyer

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
import com.example.farmermarket.data.remote.dto.OffersListDtoItem
import com.example.farmermarket.data.remote.dto.OrdersResponseDtoItem
import com.example.farmermarket.data.remote.dto.ProductDetailDto

enum class OrdersScreens {
    OFFERS,
    ORDERS,
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdersScreen(navController: NavController, viewModel: BuyerViewModel) {

    val showScreen = viewModel.showOrderScreen

    val scrollState = rememberScrollState()


    Column(horizontalAlignment = Alignment.CenterHorizontally) {

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
                horizontalArrangement = Arrangement.SpaceAround

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
                    text = "Orders",
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(30.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            viewModel.showOrderScreen.value = OrdersScreens.ORDERS
                        },
                    color = if (showScreen.value == OrdersScreens.ORDERS) {
                        Color(0xFF2CB56C)
                    } else {
                        Color(0xFFD0D1E3)
                    }
                )
            }


            when (showScreen.value) {
                OrdersScreens.OFFERS -> {

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
                                navController.navigate(BuyerScreens.OFFER_DETAILS.name)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item {
                            Spacer(modifier = Modifier.height(70.dp))
                        }

                    }


                }

                OrdersScreens.ORDERS -> {

                    LaunchedEffect(Unit) {
                        viewModel.getOrders()
                    }
                    val orders = viewModel.orderListState.value.data

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        items(orders) { order ->

                            OrderCard(order) {
                                viewModel.selectedOrder.value = order
                                navController.navigate(BuyerScreens.ORDER_DETAILS.name)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item {
                            Spacer(modifier = Modifier.height(70.dp))
                        }

                    }

                }


            }

        }

//        CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

    }
    when (showScreen.value) {
        OrdersScreens.OFFERS -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (viewModel.offerListState.value.isLoading) {
                    CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

                } else if (viewModel.offerListState.value.error != "") {
                    Text(text = "Network error")

                } else if (viewModel.offerListState.value.data.isEmpty()) {
                    Text(text = "Your orders will appear here")
                }
            }
        }

        OrdersScreens.ORDERS -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (viewModel.orderListState.value.isLoading) {
                    CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

                } else if (viewModel.orderListState.value.error != "") {
                    Text(text = "Network error")

                } else if (viewModel.orderListState.value.data.isEmpty()) {
                    Text(text = "Your orders will appear here")
                }
            }
        }


        else -> {}
    }
}

//data class OfferDto(
//    val productDTO: ProductDetailDto,
//    val amount: Double,
//    val orderPrice: Double,
//    val offeredPrice: Double,
//    val message: String
//)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderCard(ordersResponseDtoItem: OrdersResponseDtoItem, onCardClick: () -> Unit){

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
                Text(text =  "Order Id: " + ordersResponseDtoItem.id.toString(), fontSize = 24.sp, fontWeight = FontWeight.SemiBold)

                Text(text = String.format("%.2f", ordersResponseDtoItem.totalPrice) + " kzt", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4CAD73))


            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "Number of products: ${ordersResponseDtoItem.products.size}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

            Spacer(modifier = Modifier.height(15.dp))

            Text(text = "Created date: ${ordersResponseDtoItem.createdDate.substringBefore('T')}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

            Spacer(modifier = Modifier.height(15.dp))


        }



    }


}
@Composable
fun OfferCard(offer: OffersListDtoItem, onCardClick: () -> Unit){

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
                    Text(text =  offer.product.name, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = offer.product.price.toString(), fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4CAD73))
                        Text(text = "/"+ offer.product.unit, fontSize = 14.sp)

                    }

                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(text = "Amount: ${offer.product.quantity}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

                Spacer(modifier = Modifier.height(15.dp))

                Text(text = "Product price: ${offer.product.price} KZT", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

                Spacer(modifier = Modifier.height(15.dp))

                Text(text = "Offered price: ${offer.price} KZT", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

                Spacer(modifier = Modifier.height(15.dp))

                Text(text = "Message: ${offer.message} ", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFBDBDBD))

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = when (offer.isAccepted) {
                        true -> "Accepted"
                        false -> "Rejected"
                        null -> "Waiting for farmer response"
                        else -> {""}
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFBDBDBD)
                )


                Spacer(modifier = Modifier.height(7.dp))




            }



        }

}


@Composable
fun ProductOrderCart(){

}
