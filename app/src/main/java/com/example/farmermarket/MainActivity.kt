package com.example.farmermarket

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.farmermarket.common.Constants
import com.example.farmermarket.presentation.MyViewModel
import com.example.farmermarket.presentation.screens.common.StartScreen
import com.example.farmermarket.presentation.screens.common.SuccessScreen
import com.example.farmermarket.presentation.screens.common.VerificationScreen
import com.example.farmermarket.presentation.screens.auth_buyer.BuyerLoginScreen
import com.example.farmermarket.presentation.screens.auth_buyer.BuyerRegistrationScreen
import com.example.farmermarket.presentation.screens.auth_farmer.FarmerLoginScreen
import com.example.farmermarket.presentation.screens.auth_farmer.FarmerRegistrationScreen
import com.example.farmermarket.presentation.screens.main_farmer.FarmerNavigation
import dagger.hilt.android.AndroidEntryPoint


enum class Screens {
    START_SCREEN,
    BUYER_LOGIN,
    FARMER_LOGIN,
    BUYER_REGISTRATION,
    FARMER_REGISTRATION,
    VERIFICATION,
    SUCCESS_SCREEN,
    FARMER_NAVIGATION,
    BUYER_NAVIGATION
}

enum class Role{
    BUYER,
    FARMER
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var navController: NavHostController

        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()

            val viewModel = MyViewModel()


            LaunchedEffect(Unit) {
                viewModel.getChats(Constants.userName)

            }

            NavHost(
                navController = navController,
                startDestination = Screens.START_SCREEN.name,
                enterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    ExitTransition.None
                },
            ) {
                composable(route = Screens.START_SCREEN.name) {
                    StartScreen(navController)
                }

                composable(route = Screens.FARMER_LOGIN.name) {
                    FarmerLoginScreen(navController)
                }
                composable(route = Screens.BUYER_LOGIN.name) {
                    BuyerLoginScreen(navController)
                }
                composable(route = Screens.BUYER_REGISTRATION.name) {
                    BuyerRegistrationScreen(navController)
                }
                composable(route = Screens.FARMER_REGISTRATION.name) {
                    FarmerRegistrationScreen(navController)
                }
                composable(route = Screens.VERIFICATION.name) {
                    VerificationScreen(navController)
                }
                composable(route = Screens.SUCCESS_SCREEN.name) {
                    SuccessScreen(navController)
                }
                composable(route = Screens.FARMER_NAVIGATION.name){
                    FarmerNavigation(rootNavController = navController)
                }
                composable(route = Screens.BUYER_NAVIGATION.name){
                    FarmerNavigation(rootNavController = navController)
                }

            }


        }
    }
}
