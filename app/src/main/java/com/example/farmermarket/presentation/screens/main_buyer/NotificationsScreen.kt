package com.example.farmermarket.presentation.screens.main_buyer
// Jetpack Compose core libraries
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun NotificationScreen() {
    LazyColumn {
        item {
            NotificationCard(
                status = "Your order is being packed",
                statusColor = Color(0xFF4CAD73),
                productName = "Fresh Carrot",
                orderQuantity = "50 kg",
                imageUrl = "https://main-cdn.sbermegamarket.ru/big2/hlr-system/-34/440/660/684/197/100032801902b4.jpg",
                onViewDetailsClick = { /* Handle View Details */ }
            )
        }
        item {
            NotificationCard(
                status = "Your order is delivered",
                statusColor = Color(0xFF4CAD73),
                productName = "Fresh Carrot",
                orderQuantity = "50 kg",
                imageUrl = "https://main-cdn.sbermegamarket.ru/big2/hlr-system/-34/440/660/684/197/100032801902b4.jpg",
                additionalMessage = "Please, confirm the delivery:",
                actionButtonText = "I received the order",
                onViewDetailsClick = { /* Handle View Details */ },
                onActionButtonClick = { /* Handle Action */ }
            )
        }
        item {
            NotificationCard(
                status = "Your order has been cancelled",
                statusColor = Color(0xFFEB5757),
                productName = "Fresh Carrot",
                orderQuantity = "50 kg",
                imageUrl = "https://main-cdn.sbermegamarket.ru/big2/hlr-system/-34/440/660/684/197/100032801902b4.jpg",
                onViewDetailsClick = { /* Handle View Details */ }
            )
        }
    }
}


@Composable
fun NotificationCard(
    status: String,
    statusColor: Color,
    productName: String,
    orderQuantity: String,
    imageUrl: String,
    additionalMessage: String? = null,
    onViewDetailsClick: () -> Unit,
    onActionButtonClick: (() -> Unit)? = null,
    actionButtonText: String? = null
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Status Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(statusColor, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = status,
                    color = Color.White,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Content Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Product Image
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Product Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = productName,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Order: $orderQuantity",
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Additional Message
            additionalMessage?.let {
                Text(
                    text = it,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onViewDetailsClick) {
                    Text(text = "View order details", color = Color(0xFF4CAD73))
                }

                onActionButtonClick?.let {
                    Button(
                        onClick = it,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFC107))
                    ) {
                        Text(text = actionButtonText ?: "", color = Color.White)
                    }
                }
            }
        }
    }
}
