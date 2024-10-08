package com.example.farmermarket

import ChatScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.farmermarket.common.Constants
import com.example.farmermarket.presentation.MyViewModel
import com.example.farmermarket.ui.theme.GoogleBooksApiDirectTestTheme
import com.example.testproj.presentation.screens.ChatListScreen
import dagger.hilt.android.AndroidEntryPoint


enum class Screens {
    CHATLIST,
    CHAT,
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                startDestination = Screens.CHATLIST.name,
                enterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    ExitTransition.None
                },
            ) {
                composable(route = Screens.CHATLIST.name) {
                    ChatListScreen(navController, viewModel)
                }
                composable(route = Screens.CHAT.name) {
                    ChatScreen(navController, viewModel)
                }
            }


        }
    }
}
