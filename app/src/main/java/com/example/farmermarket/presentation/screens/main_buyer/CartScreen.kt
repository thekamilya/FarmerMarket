package com.example.farmermarket.presentation.screens.main_buyer

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.farmermarket.R
import com.example.farmermarket.common.Constants
import com.example.farmermarket.common.Constants.uuid
import com.example.farmermarket.data.remote.dto.CreateOrderDto
import com.example.farmermarket.data.remote.dto.Product
import com.example.farmermarket.data.remote.dto.ProductDetailDto

data class OrderDto(
    val productId: Int,
    var amount: Int,
    var soldPrice: Double
)


@Composable
fun CartScreen(viewModel: BuyerViewModel) {

    LaunchedEffect(Unit) {
        viewModel.getCart(uuid)

    }
    val products = viewModel.cartListState.value.cartResponse


    var selectedItems = remember { mutableStateListOf<ProductDetailDto>() }
    var selectedOrderList = remember { mutableListOf<OrderDto>() }
    val allSelected = remember { mutableStateOf(false)}

    val orderList = remember {
        mutableStateListOf<OrderDto>().apply {
            addAll(
                viewModel.cartListState.value.cartResponse.map { OrderDto(it.id, 1, it.price) }
            )
        }
    }


    if(viewModel.cartListState.value.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = Color(0xff4CAD73), strokeWidth = 4.dp)
        }
    }else if (viewModel.cartListState.value.error != ""){
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "Network error")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,){
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



    Box(modifier = Modifier.fillMaxSize()) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Cart", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Spacer(
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .height(0.5.dp)
            )


            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 60.dp), // Leave space for the button,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

//                item {
//                    Row(modifier = Modifier
//                        .padding(8.dp)
//                        .fillMaxWidth(),
//                    ) {
//                        Checkbox(
//                            checked = allSelected.value ,
//                            onCheckedChange = { checked ->
//                                if (!checked) {
//                                    selectedItems.clear()
//                                    selectedItems.addAll(viewModel.cartListState.value.cartResponse)
//                                    selectedOrderList.clear()
//                                    for (order in orderList){
//                                        selectedOrderList.add(order)
//                                    }
//                                } else {
//                                    selectedItems.clear()
//                                    selectedOrderList.clear()
//                                }
//                                allSelected.value = !allSelected.value
//                            },
//                            modifier = Modifier.size(24.dp),
//                            colors = CheckboxDefaults.colors(
//                                checkedColor = Color(0xFF4CAD73),
//                                uncheckedColor = Color.Gray
//                            )
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//
//                        Text(
//                            text = "Select All",
//                            fontSize = 14.sp,
//                            color = Color(0XFF828282)
//                        )
//
//
//                    }
//                    Spacer(modifier = Modifier.height(16.dp))
//                }

                itemsIndexed(products) { index, product ->

                    CartElement(
                        isSelected = selectedItems.contains(product),
                        product = product,
                        index = index,
                        onUnCheck = {
                            selectedItems.remove(product)
                            selectedOrderList.removeIf { it.productId == product.id}
                            allSelected.value = false
                        },
                        onCheck = {amount, totalPrice ->
                            selectedItems.add(product) // Add to selected items
                            selectedOrderList.add(OrderDto(product.id, amount, totalPrice))
                        },
                        onAmountChange = { amount, totalPrice ->
                            selectedOrderList.find { it.productId == product.id }?.apply {
                                this.amount = amount
                                this.soldPrice = totalPrice
                            }
                        },
                        onAmountChangeAll = { amount, totalPrice ->
                            orderList.find { it.productId == product.id }?.apply {
                                this.amount = amount
                                this.soldPrice = totalPrice
                            }
                        }
                    )

                }
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(60.dp)
                .fillMaxWidth(),
            onClick = {
                val list = mutableListOf<Product>()
                for (order in selectedOrderList){
                    list.add(Product(order.productId, order.amount.toDouble(), order.soldPrice))
                }
                val createOrderDto = CreateOrderDto(uuid,list)
                viewModel.createOrder(createOrderDto)
                Log.d("kama", selectedOrderList.toString())
            },
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Color(0xFF4CAD73))
        ) {

            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "",  // For accessibility purposes
                tint = Color.White,
                modifier = Modifier.size(20.dp) // Set the size of the icon
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Order", color = Color.White, fontSize = 18.sp)
        }
    }

}
@Composable
fun CartElement(isSelected: Boolean, product: ProductDetailDto, index:Int,
                onUnCheck:()->Unit, onCheck:(amount:Int, totalPrice: Double)->Unit,
                onAmountChange:(amount:Int, totalPrice: Double)-> Unit,
                onAmountChangeAll:(amount:Int, totalPrice: Double)-> Unit) {


        val amount = remember {
            mutableStateOf(1)
        }
        val totalPrice = remember {
            mutableStateOf(product.price)

        }

        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    if (isSelected) {
                        onUnCheck()
                    } else {
                        onCheck(amount.value, totalPrice.value)
                    }
                }
        ) {

            Checkbox(
                checked = isSelected,
                onCheckedChange = { checked ->
                    if (checked) {
                        onCheck(amount.value, totalPrice.value)
                    } else {
                        onUnCheck()
                    }
                },
                modifier = Modifier.size(24.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF4CAD73),
                    uncheckedColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.width(4.dp))


            Column(
                Modifier
                    .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(30.dp))
                    .width(322.dp)
                    .padding(24.dp)
                    .clip(RoundedCornerShape(10.dp))
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = product.name, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = product.price.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF4CAD73)
                        )
                        Text(text = "/ ${product.unit}", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Available: ${product.quantity} ${product.unit}",
                        fontSize = 14.sp,
                        color = Color(0XFF828282)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Price: ${product.price}",
                        fontSize = 14.sp,
                        color = Color(0XFF828282)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Total Price: ${String.format("%.2f", totalPrice.value)} ${"kzt"} ",
                        fontSize = 14.sp,
                        color = Color(0XFF828282)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Spacer(
                        modifier = Modifier
                            .background(Color.Gray)
                            .fillMaxWidth()
                            .height(0.5.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier.size(30.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                            border = BorderStroke(1.dp, color = Color.Gray),
                            contentPadding = PaddingValues(0.dp),
                            onClick = {
                                if (amount.value - 1 > 0) {
                                    amount.value -= 1
                                    totalPrice.value -= product.price
                                    if (isSelected) {
                                        onAmountChange(amount.value, product.price) // no need to total price
                                    }
                                    onAmountChangeAll(amount.value,  product.price) // no need to total price
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
                                if (isSelected) {
                                    onAmountChange(amount.value,  product.price)
                                }
                                onAmountChangeAll(amount.value,  product.price)
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
        }
    }
}

