package com.example.farmermarket.presentation.screens.main_buyer

import android.provider.SyncStateContract.Constants
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.PendingIntentCompat.send
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.farmermarket.R
import com.example.farmermarket.common.Constants.uuid
import com.example.farmermarket.data.remote.dto.CreateOfferDto
import com.example.farmermarket.data.remote.dto.CreateOrderDto
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.presentation.screens.main_farmer.FarmerScreens
import com.example.farmermarket.presentation.screens.main_farmer.Product
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.WormIndicatorType
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.ws
import io.ktor.http.HttpMethod
import okhttp3.internal.wait
import io.ktor.http.Url
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import org.json.JSONObject


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailsScreen(navController: NavController, viewModel: BuyerViewModel){





//    val product = ProductDetailDto(
//        category = "Vegetables",
//        description = "Fresh organic carrots",
//        farmId = 101,
//        id = 1,
//        imageUrls = listOf(
//            "https://main-cdn.sbermegamarket.ru/big2/hlr-system/-34/440/660/684/197/100032801902b4.jpg",
//            "https://main-cdn.sbermegamarket.ru/big2/hlr-system/-34/440/660/684/197/100032801902b4.jpg"
//        ),
//        name = "Organic Carrot",
//        price = 1.99,
//        quantity = 10.0,
//        unit = "kg"
//    )
    val product = viewModel.selectedProduct.value
//    viewModel.connectStomp(product.id, onDelete = {
//        navController.popBackStack()
//    })
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { product.imageUrls.size })
    val pageCount by remember { mutableStateOf(product.imageUrls.size) }
    val showDialog = remember{ mutableStateOf(false) }
    val actionName = remember{ mutableStateOf("Add to cart") }



    Box(){
        if (showDialog.value) {
            BlurredBackgroundDialog(viewModel, onDismissRequest = { showDialog.value= false }) {
                ProductActionWindow(actionName = actionName.value, product = product, viewModel)
            }
        }

        HorizontalPager(

            state = pagerState,
            pageSize = PageSize.Fill,
        ) {page ->
            AsyncImage(
                model = product.imageUrls[page] ,
                contentDescription = "Image loaded from URL",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f) // Adjust aspect ratio as needed
                ,
                onError = { error ->
                    Log.d("CoilError", "Image failed to load: ${error.result.throwable}")
                },
                contentScale = ContentScale.Crop,
                alignment = Alignment.BottomCenter
            )

        }


        Column(modifier = Modifier
            .padding(24.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                navController.popBackStack()
            }
        ) {
            Column (modifier = Modifier
                .size(40.dp)
                .background(Color(0x79000000)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }


        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 250.dp)
            .align(Alignment.BottomCenter)) {
            DotsIndicator(
                dotCount = product.imageUrls.size,
                type = WormIndicatorType(
                    dotsGraphic = DotGraphic(
                        7.dp,
                        borderWidth = 2.dp,
                        borderColor = Color(0xFFC4C4C4) ,
                        color = Color(0xFFC4C4C4),
                    ),
                    wormDotGraphic = DotGraphic(
                        7.dp,
                        color = Color(0xFFFFFFFF)
                    )
                ),
                pagerState = pagerState,
                modifier = Modifier
                    .padding(bottom = 20.dp)
            )
            Column(modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topEnd = 30.dp, topStart = 30.dp))
                .background(Color.White)
            ) {

                Column(Modifier.padding(24.dp)){

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text =  product.name, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = product.price.toString(), fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4CAD73))
                        Text(text = "/ "+ product.unit, fontSize = 18.sp)

                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "Available: ${product.quantity} kg",
                            fontSize = 14.sp,
                            color = Color(0XFF828282)
                        )
                    }
                    Spacer(modifier = Modifier.height(7.dp))


                }
                Spacer(modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .height((0.5).dp))

                Column(Modifier.padding(24.dp)){
                    Spacer(modifier = Modifier.height(7.dp))

                    Text(text = product.description,
                        fontSize = 14.sp, color = Color(0XFF828282))

                    Spacer(modifier = Modifier.height(100.dp))

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color(0xFFF2F2F2))
                            .fillMaxWidth()
                            .height(67.dp)
                            .padding(horizontal = 25.dp)
                            ,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {



                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = rememberAsyncImagePainter(model = R.drawable.avatarimage),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(35.dp)
                                    .clip(CircleShape)
                                ,
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
                                viewModel.createNewChat(product.farmId.toString()) {
                                    viewModel.selectedParticipantName.value = product.farmId.toString()
                                    viewModel.getChat(it, uuid)
                                    navController.navigate(FarmerScreens.CHAT.name)
                                }

                            })
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    val context = LocalContext.current
                    Row {
                        Button(
                            modifier = Modifier
                                .height(40.dp)
                                .weight(1f)  // Set the weight to equally divide the space
                                .fillMaxWidth(),
                            onClick = {
                                viewModel.addToCart(uuid,product, onSuccess = {
                                    Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()
                                }, onFailure = {
                                    Toast.makeText(context, "Something went wrong! Try later", Toast.LENGTH_SHORT).show()
                                })
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFFFAB663))

                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.cart_icon), // You can use any vector icon here
                                contentDescription = "Add to cart ",  // For accessibility purposes
                                tint = Color.White,
                                modifier = Modifier.size(20.dp) // Set the size of the icon
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Add to cart", color = Color.White, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            modifier = Modifier
                                .height(40.dp)
                                .weight(1f)  // Set the weight to equally divide the space
                                .fillMaxWidth()
                            ,
                            onClick = {
                                actionName.value = "Order"
                                showDialog.value = true

                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color(0XFF2CB56C))
                        ) {

                            Icon(
                                imageVector = Icons.Default.Check, // You can use any vector icon here
                                contentDescription = "Order",  // For accessibility purposes
                                tint = Color.White,
                                modifier = Modifier.size(20.dp) // Set the size of the icon
                            )

                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = "Order", color = Color.White, fontSize = 16.sp)
                        }




                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            modifier = Modifier
                                .height(40.dp)
                                .weight(1f)  // Set the weight to equally divide the space
                                .fillMaxWidth()
                            ,
                            onClick = {
                                actionName.value = "Offer"
                                showDialog.value = true
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color(0XFFF68672))
                        ) {

                            Icon(
                                imageVector = Icons.Default.Percent, // You can use any vector icon here
                                contentDescription = "Offer",  // For accessibility purposes
                                tint = Color.White,
                                modifier = Modifier.size(20.dp) // Set the size of the icon
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Offer", color = Color.White, fontSize = 16.sp)
                        }
                    }
                }

            }
        }
    }


}

@Composable
fun ProductActionWindow(actionName: String, product: ProductDetailDto, viewModel: BuyerViewModel){

    val amount = remember {
        mutableStateOf(1)
    }
    val totalPrice = remember {
        mutableStateOf(product.price)
    }

    var offeredPriceText by remember { mutableStateOf("") }

    var offeredPrice by remember { mutableStateOf(0.0) }

    var message by remember { mutableStateOf("") }


    Column(
        Modifier
            .padding(24.dp)
            .clip(RoundedCornerShape(10.dp))){

        Spacer(modifier = Modifier.height(20.dp))
        Text(text =  product.name, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = product.price.toString(), fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4CAD73))
            Text(text = "/ "+ product.unit, fontSize = 18.sp)

        }
        Spacer(modifier = Modifier.height(14.dp))


        Text(
            text = "Available: ${product.quantity} ${product.unit} ",
            fontSize = 14.sp,
            color = Color(0XFF828282)
        )

        Spacer(modifier = Modifier.height(14.dp))


        Text(
            text = "Total Price: ${String.format("%.2f", totalPrice.value)} ${"kzt"} ",
            fontSize = 14.sp,
            color = Color(0XFF828282))

        Spacer(modifier = Modifier.height(14.dp))

        if (actionName == "Offer"){

            Column {
                Column() {
                    Text(
                        text = "Offer Price:",
                        fontSize = 14.sp,
                        color = Color(0XFF828282)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    TextField(

                        shape = RoundedCornerShape(10.dp),
                        placeholder = { Text(text = "0.00", color = Color(0xFFBDBDBD)) },
                        maxLines = 1,
                        colors = TextFieldDefaults.colors(
                            focusedLabelColor = Color.DarkGray,
                            focusedContainerColor = Color(0xFFF2F2F2),
                            unfocusedContainerColor = Color(0xFFF2F2F2),
                            disabledContainerColor = Color(0xFFF2F2F2),
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color(0xFFF2F2F2),
                            unfocusedIndicatorColor = Color(0xFFF2F2F2),
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                        ,
                        value = offeredPriceText,
                        onValueChange = {
                            offeredPriceText = it
                            offeredPrice = it.toDoubleOrNull() ?: 0.0
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        isError = offeredPriceText.isNotEmpty() && offeredPriceText.toDoubleOrNull() == null,
                        textStyle = TextStyle(
                            fontSize = 14.sp, // Adjust font size here
                            color = Color.Black // Ensure the text is visible
                        ),
                    )
                }

                    Spacer(modifier = Modifier.height(14.dp))

                    Column() {
                        Text(
                            text = "Message:",
                            fontSize = 14.sp,
                            color = Color(0XFF828282)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        OutlinedTextField(

                            shape = RoundedCornerShape(10.dp),
                            placeholder = { Text(text = "Please, lower the price!", color = Color(0xFFBDBDBD)) },
                            maxLines = 1,
                            colors = TextFieldDefaults.colors(
                                focusedLabelColor = Color.DarkGray,
                                focusedContainerColor = Color(0xFFF2F2F2),
                                unfocusedContainerColor = Color(0xFFF2F2F2),
                                disabledContainerColor = Color(0xFFF2F2F2),
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color(0xFFF2F2F2),
                                unfocusedIndicatorColor = Color(0xFFF2F2F2),
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent)


                            ,
                            value = message,
                            onValueChange = {
                                message = it

                            },
                            textStyle = TextStyle(
                                fontSize = 14.sp, // Adjust font size here
                                color = Color.Black // Ensure the text is visible
                            ),
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

            }

        }
        Spacer(modifier = Modifier
            .background(Color.Gray)
            .fillMaxWidth()
            .height((0.5).dp))

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically) {

            Button(
                modifier = Modifier.size(30.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                border = BorderStroke(1.dp, color = Color.Gray),
                contentPadding = PaddingValues(0.dp),
                onClick = {

                    if(amount.value - 1 > 0){
                        amount.value -= 1
                        totalPrice.value -= product.price
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.HorizontalRule,
                    contentDescription = "Minus Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = " ${amount.value} ${product.unit} ",
                fontSize = 14.sp,
                color = Color(0XFF828282)
            )

            Button(
                modifier = Modifier.size(30.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                border = BorderStroke(1.dp, color = Color.Gray),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    amount.value += 1
                    totalPrice.value += product.price
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Plus Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }


        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier
                .height(40.dp) // Set the weight to equally divide the space
                .fillMaxWidth()
            ,
            onClick = {

                when (actionName) {

                    "Order" -> {
                        val createOrderDto = CreateOrderDto(uuid, listOf(com.example.farmermarket.data.remote.dto.Product(product.id,amount.value.toDouble(), product.price.toDouble())))
                        viewModel.createOrder(createOrderDto)
                    }
                    "Offer" -> {
                        val createOfferDto = CreateOfferDto(message, offeredPrice.toInt(), product.id)
                        viewModel.createOffer(createOfferDto)

                    }
                    "Add to cart" -> {}
                    else -> {}
                }

            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                when (actionName) {

                    "Order" -> Color(0xFF4CAD73)
                    "Offer" -> Color(0xFFF68672)
                    "Add to cart" -> Color(0xFFFAB663)
                    else -> Color.Gray
                }
            )
        ) {

            Icon(
                imageVector = when (actionName) {

                    "Order" -> Icons.Default.Check
                    "Offer" -> Icons.Default.Percent
                    "Add to cart" -> ImageVector.vectorResource(id = R.drawable.cart_icon)
                    else -> Icons.Default.Percent
                }, // You can use any vector icon here
                contentDescription = actionName,  // For accessibility purposes
                tint = Color.White,
                modifier = Modifier.size(20.dp) // Set the size of the icon
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(text = actionName, color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,){
//            CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)
            if(viewModel.orderCreationState.value.isLoading){
                CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

            }else if(viewModel.orderCreationState.value.error != ""){
                Toast.makeText(LocalContext.current, "Something went wrong! Try later", Toast.LENGTH_SHORT).show()
                viewModel.orderCreationState.value = viewModel.orderCreationState.value.copy(error = "")


            } else if(viewModel.orderCreationState.value.created == true){
                Toast.makeText(LocalContext.current, "Successfully created", Toast.LENGTH_SHORT).show()
                viewModel.orderCreationState.value = viewModel.orderCreationState.value.copy(created = null)
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,){
//            CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)
            if(viewModel.offerCreationState.value.isLoading){
                CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

            }else if(viewModel.offerCreationState.value.error != ""){
                Toast.makeText(LocalContext.current, "Something went wrong! Try later", Toast.LENGTH_SHORT).show()
                viewModel.offerCreationState.value = viewModel.offerCreationState.value.copy(error = "")


            } else if(viewModel.offerCreationState.value.created == true){
                Toast.makeText(LocalContext.current, "Successfully created", Toast.LENGTH_SHORT).show()
                viewModel.offerCreationState.value = viewModel.offerCreationState.value.copy(created = null)
            }
        }

    }


}
@Composable
fun BlurredBackgroundDialog(
    viewModel: BuyerViewModel,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    // Overlay with a blurred background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6F)) // Semi-transparent background
            .clickable(onClick = onDismissRequest) // Dismiss on outside click
    ) {
        Dialog(onDismissRequest = onDismissRequest) {
            // Dialog content
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}