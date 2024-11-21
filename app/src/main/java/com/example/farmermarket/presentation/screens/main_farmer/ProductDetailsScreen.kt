package com.example.farmermarket.presentation.screens.main_farmer

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.farmermarket.R
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.WormIndicatorType
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText

//
import okhttp3.internal.wait
import org.json.JSONObject


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailsScreen(navController: NavController, viewModel: FarmerViewModel){




    val product = viewModel.selectedProduct.value
    viewModel.connectStomp(product.id, onDelete = {
        navController.popBackStack()
    })
//    val imageNames by viewModel.imageNames

    // Set up pager state
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { product.imageUrls.size })
    val pageCount by remember { mutableStateOf(viewModel.imageNames.value.size) }
    var showDialog by remember { mutableStateOf(false) }

        Box(){
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Are you sure you want to delete the product?", fontSize = 18.sp) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                // Call the delete product function
                                viewModel.deleteProduct(product.id, onReady = {
                                    // Fetch products again after deleting
                                    viewModel.getProducts()
                                    // Pop back the stack (navigate back)
                                    navController.popBackStack()
                                })
                                // Close the dialog after confirming
                                showDialog = false
                            }
                        ) {
                            Text("Yes", color = Color(0xFF4CAD73))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                // Close the dialog when dismissing
                                showDialog = false
                            }
                        ) {
                            Text("No", color = Color(0xFF4CAD73))
                        }
                    }
                )
            }

            HorizontalPager(

                state = pagerState,
                pageSize = PageSize.Fill,
            ) {page ->
                AsyncImage(
                    model = viewModel.selectedProduct.value.imageUrls[page] ,
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


            Column(modifier = Modifier.padding(24.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                navController.popBackStack()
                }
            ) {
                Column (modifier = Modifier.size(40.dp)
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
                    dotCount = viewModel.selectedProduct.value.imageUrls.size,
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
                        Row {
                            Button(
                                modifier = Modifier
                                    .height(40.dp)
                                    .weight(1f)  // Set the weight to equally divide the space
                                    .fillMaxWidth(),
                                onClick = { showDialog = true},
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFFE44460))

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete, // You can use any vector icon here
                                    contentDescription = "Delete Icon",  // For accessibility purposes
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp) // Set the size of the icon
                                )

                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Delete", color = Color.White, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                modifier = Modifier
                                    .height(40.dp)
                                    .weight(1f)  // Set the weight to equally divide the space
                                    .fillMaxWidth()
                                    ,
                                onClick = {
                                          navController.navigate(FarmerScreens.EDIT_PRODUCT.name)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFFF6F6F6))
                            ) {

                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.edit_icon), // You can use any vector icon here
                                    contentDescription = "Edit Icon",  // For accessibility purposes
                                    tint = Color.Black,
                                    modifier = Modifier.size(20.dp) // Set the size of the icon
                                )

                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Edit", color = Color.Black, fontSize = 16.sp)
                            }
                        }
                    }
                    
                }
            }
    }

}