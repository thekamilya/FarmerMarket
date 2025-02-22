package com.example.farmermarket.presentation.screens.main_farmer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.farmermarket.R
import com.example.farmermarket.Screens
import com.google.accompanist.systemuicontroller.rememberSystemUiController

enum class FarmerScreens {
    MARKET,
    ORDERS,
    CHATS,
    CHAT,
    PROFILE,
    PRODUCT_DETAILS,
    EDIT_PRODUCT,
    ADD_PRODUCT,
    ORDER_DETAILS,
    OFFER_DETAILS

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
                    Color(0xFF4CAD73)}
                else {
                    Color.Gray}
            )
            Spacer(modifier = Modifier.height(5.dp))
        }

    }
}

@Composable
fun BottomBar(navController: NavHostController, selectedRoute: String, onSelectedChange:(String) -> Unit ){
    Box(modifier = Modifier
        .shadow(
            elevation = 16.dp,
            shape = RoundedCornerShape(topEnd = 34.dp, topStart = 34.dp), clip = true
        )
        .background(Color.White)
        .clip(RoundedCornerShape(topEnd = 34.dp, topStart = 34.dp))){
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
        ) {

            NavigationItem(onClick = {
                navController.navigate(FarmerScreens.MARKET.name)
                onSelectedChange(FarmerScreens.MARKET.name)
            }, thisRoute =FarmerScreens.MARKET.name,
                selectedRoute = selectedRoute,
                title = "Market", imageVector = ImageVector.vectorResource(id = R.drawable.market_nav) )

            NavigationItem(onClick = {
                navController.navigate(FarmerScreens.ORDERS.name)
                 onSelectedChange(FarmerScreens.ORDERS.name)
            },selectedRoute = selectedRoute,
                thisRoute =FarmerScreens.ORDERS.name,
                title = "Orders", imageVector = ImageVector.vectorResource(R.drawable.order_nav) )

            NavigationItem(onClick = {
                navController.navigate(FarmerScreens.CHATS.name)
                onSelectedChange(FarmerScreens.CHATS.name)
            },selectedRoute = selectedRoute,
                thisRoute =FarmerScreens.CHATS.name ,
                title = "Chats", imageVector = ImageVector.vectorResource(R.drawable.chats_nav) )

            NavigationItem(onClick = {
                navController.navigate(FarmerScreens.PROFILE.name)
                onSelectedChange(FarmerScreens.PROFILE.name)
            },selectedRoute = selectedRoute,
                thisRoute = FarmerScreens.PROFILE.name ,title = "Profile", imageVector = ImageVector.vectorResource(R.drawable.profile_nav) )

        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FarmerNavigation(rootNavController: NavHostController , context: Context) {


    val farmerNavController = rememberNavController()

    var selectedRoute by remember { mutableStateOf(FarmerScreens.MARKET.name) }
    val currentBackStackEntry by farmerNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = true // Change to false if you want light icons on a dark background

    val viewModel: FarmerViewModel = hiltViewModel()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Gray
    ) {
        Scaffold(
            bottomBar = { if (
                currentRoute == FarmerScreens.MARKET.name
                || currentRoute == FarmerScreens.ORDERS.name
                || currentRoute == FarmerScreens.CHATS.name
                || currentRoute == FarmerScreens.PROFILE.name) {

                BottomBar(farmerNavController, selectedRoute){selected->
                    selectedRoute = selected

                }
            }}
        ) {


            // Set the status bar color

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
                    MarketScreen(farmerNavController, viewModel)

                    systemUiController.setStatusBarColor(
                        color = Color(0xff4CAD73), // Replace with your desired color
                    )
                }
                composable(route = FarmerScreens.ORDERS.name) {
                    OrdersScreen( farmerNavController, viewModel)

                    systemUiController.setStatusBarColor(
                        color = Color.White, // Replace with your desired color
                        darkIcons = useDarkIcons
                    )
                }
                composable(route = FarmerScreens.CHATS.name) {

                    ChatsScreen( farmerNavController, viewModel)

                    systemUiController.setStatusBarColor(
                        color = Color.White, // Replace with your desired color
                        darkIcons = useDarkIcons
                    )
                }
                composable(route = FarmerScreens.CHAT.name) {

                    ChatScreen( farmerNavController, viewModel)

                    systemUiController.setStatusBarColor(
                        color = Color.White, // Replace with your desired color
                        darkIcons = useDarkIcons
                    )
                }

                composable(route = FarmerScreens.PROFILE.name) {

                    ProfileScreen( farmerNavController,viewModel){
                        rootNavController.navigate(Screens.START_SCREEN.name)
                    }

                    systemUiController.setStatusBarColor(
                        color = Color(0xff4CAD73), // Replace with your desired color
                    )
                }

                composable(route = FarmerScreens.PRODUCT_DETAILS.name){

                    ProductDetailsScreen(farmerNavController, viewModel)

                    systemUiController.setStatusBarColor(
                        color = Color.Transparent, // Replace with your desired color
                        darkIcons = useDarkIcons
                    )
                }

                composable(route = FarmerScreens.EDIT_PRODUCT.name){

                    EditProductScreen(farmerNavController, viewModel)

                    systemUiController.setStatusBarColor(
                        color = Color.Transparent, // Replace with your desired color
                        darkIcons = useDarkIcons
                    )
                }
                composable(route = FarmerScreens.ADD_PRODUCT.name){

                    AddProductScreen(farmerNavController, viewModel, context )

                    systemUiController.setStatusBarColor(
                        color = Color.Transparent, // Replace with your desired color
                        darkIcons = useDarkIcons
                    )
                }
                composable(route = FarmerScreens.ORDER_DETAILS.name){

                    OrderDetailsScreen(farmerNavController, viewModel)

                    systemUiController.setStatusBarColor(
                        color = Color(0xff4CAD73), // Replace with your desired color
                        darkIcons = useDarkIcons
                    )
                }
                composable(route = FarmerScreens.OFFER_DETAILS.name){

                    OfferDetailsScreen(farmerNavController, viewModel)

                    systemUiController.setStatusBarColor(
                        color = Color(0xff4CAD73), // Replace with your desired color
                        darkIcons = useDarkIcons
                    )
                }

            }
        }
    }
}
