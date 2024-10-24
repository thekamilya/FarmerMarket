package com.example.farmermarket.presentation.screens.main_buyer

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
//
//enum class HomeScreens {
//    LIBRARY,
//    SEARCH,
//    PROFILE,
//
//}
//
//
//@Composable
//fun NavigationItem(onClick: () -> Unit,thisRoute:String, selectedRoute:String, title: String,imageVector: ImageVector){
//
//    Box(modifier = Modifier
//        .clickable(onClick = {onClick()}) ){
//        Column(horizontalAlignment = Alignment.CenterHorizontally){
//            Icon(imageVector = imageVector ,
//                contentDescription = title,
//                tint = if(thisRoute == selectedRoute) {
//                    Color.White} else {
//                    Color.Gray}
//            )
//            Spacer(modifier = Modifier.height(5.dp))
//            Text(text = title, fontSize = 11.sp, fontFamily = FontFamily(Font(R.font.my_custom_font_medium)),
//                color = if(thisRoute == selectedRoute) {
//                    Color.White} else {
//                    Color.Gray}
//            )
//        }
//
//    }
//}
//
//@Composable
//fun BottomBar(navController: NavHostController){
//    Box(modifier = Modifier
//        .background(
//            brush = Brush.verticalGradient(
//                listOf(Color.Transparent, Color.White),
//                startY = 0.0f,
//                endY = 100.0f,
////                tileMode = TileMode.Clamp
//            )
//        )
//        .padding(start = 10.dp,end = 10.dp, top = 50.dp, bottom = 10.dp)
//        .clip(RoundedCornerShape(16.dp))){
//        Row(modifier = Modifier
//            .fillMaxWidth()
//            .height(70.dp)
//            .background(color = Color(0xFF444A51)),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
//        ) {
//            var selectedRoute by remember { mutableStateOf(HomeScreens.LIBRARY.name) }
//
//            NavigationItem(onClick = {
//                navController.navigate(HomeScreens.LIBRARY.name)
//                selectedRoute = HomeScreens.LIBRARY.name
//            }, thisRoute = HomeScreens.LIBRARY.name,
//                selectedRoute = selectedRoute,
//                title = "Library", imageVector = ImageVector.vectorResource(R.drawable.library) )
//
//            NavigationItem(onClick = {
//                navController.navigate(HomeScreens.SEARCH.name)
//                selectedRoute = HomeScreens.SEARCH.name
//            },selectedRoute = selectedRoute,
//                thisRoute = HomeScreens.SEARCH.name, title = "Discover", imageVector = ImageVector.vectorResource(R.drawable.search) )
//
//            NavigationItem(onClick = {
//                navController.navigate(HomeScreens.PROFILE.name)
//                selectedRoute = HomeScreens.PROFILE.name
//            },selectedRoute = selectedRoute,
//                thisRoute = HomeScreens.PROFILE.name ,title = "Profile", imageVector = ImageVector.vectorResource(R.drawable.profile) )
//
//        }
//    }
//
//
//}
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun HomeScreen(rootNavController: NavHostController) {
//    val homeNavController = rememberNavController()
//    var selectedRoute by remember { mutableStateOf(HomeScreens.LIBRARY) }
//
//
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//    ) {
//        Scaffold(
//            bottomBar = { BottomBar(homeNavController) }
//        ) {
//            NavHost(
//                navController = homeNavController,
//                startDestination = HomeScreens.LIBRARY.name,
//                enterTransition = {
//                    EnterTransition.None
//                },
//                exitTransition = {
//                    ExitTransition.None
//                },
//            ) {
//                composable(route = HomeScreens.LIBRARY.name) {
//                    LibraryNavScreen()
//                }
//                composable(route = HomeScreens.SEARCH.name) {
//                    DiscoverNavScreen()
//                }
//                composable(route = HomeScreens.PROFILE.name) {
//
//                    ProfileNavScreen(onLogOut = {
//                        rootNavController.navigate(route = MainScreens.AUTH.name)
//
//                    })
//                }
//
//            }
//        }
//    }
//}
