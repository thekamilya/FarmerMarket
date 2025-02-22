package com.example.farmermarket.presentation.screens.main_farmer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.farmermarket.R
import com.example.farmermarket.data.remote.dto.ProductResponseDtoItem
import java.io.InputStream

@Composable
fun MarketScreen( navController: NavController, viewModel: FarmerViewModel){

    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }
    val productState = viewModel.productListState.value
    val productList = viewModel.productListState.value.productResponse

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,){
        if(productState.isLoading){
            CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)

        }else if(productState.error != ""){
            Text(text = "Network error")

        }
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

    val search = remember { mutableStateOf(TextFieldValue()) }

    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        ){


        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.group_771),
            contentDescription = null, // or provide a meaningful description
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                shape = RoundedCornerShape(10.dp),
                maxLines = 1,
                value = search.value,
                onValueChange = { search.value = it },
                label = {
                    Text(
                        "Search for products..",
                        fontSize = 14.sp,
                        color = Color(0xFFBDBDBD)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color(0xFFCDCDD2),
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color(0xFFCDCDD2),
                ),
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent),
                leadingIcon = {
                    IconButton(
                        onClick = {
                            search.value = TextFieldValue("")
                        },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.search_icon),
                            contentDescription = "Search",
                            tint = Color(0xFFBDBDBD)
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            search.value = TextFieldValue("")
                        },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.filter_icon),
                            contentDescription = "Filter",
                            tint =  Color(0xFFBDBDBD)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    navController.navigate(FarmerScreens.ADD_PRODUCT.name)
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(

                    imageVector = ImageVector.vectorResource(id = R.drawable.add_icon),
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }


//        val products = List(10) {
//            Product(
//                id = "sdbsksbcd$it",
//                productName = "Fresh Carrot",
//                category = "Vegetables",
//                description = "The carrot is a root vegetable, most commonly observed as orange in color, " +
//                        "though purple, black, red, white, and yellow cultivars exist, all of which are " +
//                        "domesticated forms of the wild carrot, Daucus carota, native to Europe and Southwestern Asia.",
//                unit = "kg",
//                pricePerUnit = 15000,
//                totalReserve = 500,
//                imageUris = mutableListOf(Uri.parse("https://main-cdn.sbermegamarket.ru/big2/hlr-system/-34/440/660/684/197/100032801902b4.jpg"))
//            )
//        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 30.dp)
        ) {
            // LazyColumn displaying products
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {
                items(productList.chunked(2)) { productPair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        productPair.forEach { product ->
//                            viewModel.getImage(product.id, "0" )

                            ProductCard(
                                product = product,
                                onEditClick = {
                                    navController.navigate(FarmerScreens.EDIT_PRODUCT.name)
                                },
                                onCardClick = {

                                    navController.navigate(FarmerScreens.PRODUCT_DETAILS.name)
                                },
                                viewModel,

                            )

                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
            // Top gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp) // Adjust height for fade effect
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF4CAD73), Color.Transparent)
                        )
                    )
            )

        }
    }
}


data class Product(
    val id: String,
    val productName : String,
    val category: String,
    val description: String,
    val unit: String,
    val pricePerUnit : Int,
    val totalReserve : Int,
    val imageUris : MutableList<Uri?>
)
@Composable
fun ProductCard(product: ProductResponseDtoItem, onEditClick: () -> Unit, onCardClick: () -> Unit, viewModel: FarmerViewModel){

//    LaunchedEffect(key1 = product.id) {
//        viewModel.getFirstImage(product.id, "0")
//    }

    Box(modifier = Modifier
        .height((LocalConfiguration.current.screenHeightDp.dp) / 3f)

        .width((LocalConfiguration.current.screenWidthDp.dp) / 2.4f)
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

//            viewModel.selectedProduct.value = product
            viewModel.getProduct(product.id)
            onCardClick()
        }) {

        Column(modifier = Modifier.fillMaxSize()){


            AsyncImage(
                model = product.imageURL ,
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

            
            Spacer(modifier = Modifier.height(10.dp))

            Column(modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .fillMaxSize()) {
                Text(text = product.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(10.dp))
                
                Row {
                    Text(text =product.price.toString(), fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4CAD73))
                    Text(text = "/ "+ product.unit, fontSize = 12.sp)

                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Available: ${product.quantity} ${product.unit}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFBDBDBD),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.edit_icon) ,
                    contentDescription = "Search",
                    tint = Color(0xFF4CAD73),
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
//                            viewModel.getProduct(product.id)
//                            onEditClick()
                        },


                )

            }
            


        }




    }
    


}


fun inputStreamToBitmap(inputStream: InputStream): Bitmap? {
    return try {
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        print("exception")
        null
    }
}




//
//@Preview
//@Composable
//fun ProductCardPreview(){
//    ProductCard(product = Product(id = "sdbsksbcd", productName = "Fresh Carrot", category = "veegetables",
//        description = "The carrot is a root vegetable, most commonly observed as orange in color, " +
//                "though purple, black, red, white, and yellow cultivars exist, " +
//                "all of which are domesticated forms of the wild carrot, " +
//                "Daucus carota, native to Europe and Southwestern Asia.", unit = "kg", pricePerUnit = 15000, totalReserve = 500,
//        imageUris = mutableListOf(Uri.parse("https://main-cdn.sbermegamarket.ru/big2/hlr-system/-34/440/660/684/197/100032801902b4.jpg"))
//    ))
//
//
//}

