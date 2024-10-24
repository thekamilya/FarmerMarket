package com.example.farmermarket.presentation.screens.main_farmer

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.farmermarket.R

enum class FarmerScreens {
    MARKET,
    ORDERS,
    CHATS,
    PROFILE,

}


@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun NavigationItem(onClick: () -> Unit,thisRoute:String, selectedRoute:String, title: String,imageVector: ImageVector ){

    Box(
        modifier = Modifier.clickable(
            onClick = { onClick() },
            indication = null, // Removes the ripple effect
            interactionSource = androidx.compose.foundation.interaction.MutableInteractionSource() // Removes any visual feedback
        )

    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Icon(imageVector = imageVector,
                contentDescription = title,
                tint = if(thisRoute == selectedRoute) {
                    Color(0xFF4CAD73)} else {
                    Color.Gray}
            )
            Spacer(modifier = Modifier.height(5.dp))
        }

    }
}

@Composable
fun BottomBar(navController: NavHostController){
    Box(modifier = Modifier
        .shadow(
            elevation = 16.dp,
            shape = RoundedCornerShape(topEnd = 34.dp, topStart = 34.dp), clip = true).background(Color.White)
        .clip(RoundedCornerShape(topEnd = 34.dp, topStart = 34.dp))){
        Row(modifier = Modifier
            .fillMaxWidth().height(70.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
        ) {
            var selectedRoute by remember { mutableStateOf(FarmerScreens.MARKET.name) }

            NavigationItem(onClick = {
                navController.navigate(FarmerScreens.MARKET.name)
                selectedRoute = FarmerScreens.MARKET.name
            }, thisRoute =FarmerScreens.MARKET.name,
                selectedRoute = selectedRoute,
                title = "Market", imageVector = ImageVector.vectorResource(id = R.drawable.market_nav) )

            NavigationItem(onClick = {
                navController.navigate(FarmerScreens.ORDERS.name)
                selectedRoute = FarmerScreens.ORDERS.name
            },selectedRoute = selectedRoute,
                thisRoute =FarmerScreens.ORDERS.name, title = "Orders", imageVector = ImageVector.vectorResource(R.drawable.order_nav) )

            NavigationItem(onClick = {
                navController.navigate(FarmerScreens.CHATS.name)
                selectedRoute = FarmerScreens.CHATS.name
            },selectedRoute = selectedRoute,
                thisRoute =FarmerScreens.CHATS.name ,title = "Chats", imageVector = ImageVector.vectorResource(R.drawable.chats_nav) )

            NavigationItem(onClick = {
                navController.navigate(FarmerScreens.PROFILE.name)
                selectedRoute = FarmerScreens.PROFILE.name
            },selectedRoute = selectedRoute,
                thisRoute = FarmerScreens.PROFILE.name ,title = "Profile", imageVector = ImageVector.vectorResource(R.drawable.profile_nav) )

        }
    }


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FarmerNavigation(rootNavController: NavHostController) {
    val farmerNavController = rememberNavController()
    var selectedRoute by remember { mutableStateOf(FarmerScreens.MARKET) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Gray
    ) {
        Scaffold(
            bottomBar = { BottomBar(farmerNavController) }
        ) {
            NavHost(
                navController = farmerNavController,
                startDestination = FarmerScreens.MARKET.name,
                enterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    ExitTransition.None
                },
            ) {
                composable(route = FarmerScreens.MARKET.name) {
                    MarketScreen()
                }
                composable(route = FarmerScreens.ORDERS.name) {
                    OrdersScreen()
                }
                composable(route = FarmerScreens.CHATS.name) {

                    ChatScreen()
                }

                composable(route = FarmerScreens.PROFILE.name) {

                    ProfileScreen()
                }


            }
        }
    }
}
