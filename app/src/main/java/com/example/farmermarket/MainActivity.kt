package com.example.farmermarket

import BuyerNavigation
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.farmermarket.common.Constants.BASE_URL
import com.example.farmermarket.data.remote.FarmApi
import com.example.farmermarket.data.remote.dto.ProductDTO
import com.example.farmermarket.presentation.screens.auth_buyer.AuthViewModel
import com.example.farmermarket.presentation.screens.common.StartScreen
import com.example.farmermarket.presentation.screens.common.SuccessScreen
import com.example.farmermarket.presentation.screens.common.VerificationScreen
import com.example.farmermarket.presentation.screens.auth_buyer.BuyerLoginScreen
import com.example.farmermarket.presentation.screens.auth_buyer.BuyerRegistrationScreen
import com.example.farmermarket.presentation.screens.auth_farmer.FarmerLoginScreen
import com.example.farmermarket.presentation.screens.auth_farmer.FarmerRegistrationScreen
import com.example.farmermarket.presentation.screens.main_farmer.FarmerNavigation
import com.example.farmermarket.presentation.screens.main_farmer.FarmerViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


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

            val authViewModel: AuthViewModel = hiltViewModel()
            val systemUiController = rememberSystemUiController()

            NavHost(
                navController = navController,
                startDestination =
                Screens.START_SCREEN.name
//                if (authViewModel.getFromPreferences("token")?.isNotEmpty() == true) {
//                    Screens.BUYER_NAVIGATION.name
//                }else{
//                    Screens.START_SCREEN.name
//                     }

                ,

                enterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    ExitTransition.None
                },
            ) {
                composable(route = Screens.START_SCREEN.name) {
                    StartScreen(navController)
                    systemUiController.setStatusBarColor(
                        color = Color(0xff53B97C), // Replace with your desired color
                    )
                }

                composable(route = Screens.FARMER_LOGIN.name) {
                    FarmerLoginScreen(navController, authViewModel)
                    systemUiController.setStatusBarColor(
                        color = Color(0xff53B97C), // Replace with your desired color
                    )
                }
                composable(route = Screens.BUYER_LOGIN.name) {
                    BuyerLoginScreen(navController, authViewModel)
                    systemUiController.setStatusBarColor(
                        color = Color(0xff53B97C), // Replace with your desired color
                    )
                }
                composable(route = Screens.BUYER_REGISTRATION.name) {
                    BuyerRegistrationScreen(navController, authViewModel)
                }
                composable(route = Screens.FARMER_REGISTRATION.name) {
                    FarmerRegistrationScreen(navController,authViewModel )
                }
                composable(route = Screens.VERIFICATION.name) {
                    VerificationScreen(navController)
                }
                composable(route = Screens.SUCCESS_SCREEN.name) {
                    SuccessScreen(navController)
                }
                composable(route = Screens.FARMER_NAVIGATION.name){
                    FarmerNavigation(rootNavController = navController, context = applicationContext,)
                    navController.clearBackStack(Screens.FARMER_NAVIGATION.name)

                }
                composable(route = Screens.BUYER_NAVIGATION.name){
                    BuyerNavigation(rootNavController = navController)

                }

            }
        }
    }
}
