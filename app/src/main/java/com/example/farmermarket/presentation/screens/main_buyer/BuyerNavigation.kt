import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
import com.example.farmermarket.presentation.screens.main_buyer.BuyerViewModel
import com.example.farmermarket.presentation.screens.main_buyer.CartScreen
import com.example.farmermarket.presentation.screens.main_buyer.ChatsScreen
import com.example.farmermarket.presentation.screens.main_buyer.MarketScreen
import com.example.farmermarket.presentation.screens.main_buyer.NotificationScreen
import com.example.farmermarket.presentation.screens.main_buyer.OfferDetailsScreen
import com.example.farmermarket.presentation.screens.main_buyer.OrderDetailsScreen
import com.example.farmermarket.presentation.screens.main_buyer.OrdersScreen
import com.example.farmermarket.presentation.screens.main_buyer.ProductDetailsScreen
import com.example.farmermarket.presentation.screens.main_buyer.ProfileScreen
import com.example.farmermarket.presentation.screens.main_farmer.NavigationItem
import com.google.accompanist.systemuicontroller.rememberSystemUiController


enum class BuyerScreens {
    MARKET,
    ORDERS,
    ORDER_DETAILS,
    CHATS,
    CHAT,
    PROFILE,
    PRODUCT_DETAILS,
    CART,
    NOTIFICATIONS,
    OFFER_DETAILS
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
                navController.navigate(BuyerScreens.MARKET.name)
                onSelectedChange(BuyerScreens.MARKET.name)
            }, thisRoute = BuyerScreens.MARKET.name,
                selectedRoute = selectedRoute,
                title = "Market", imageVector = ImageVector.vectorResource(id = R.drawable.market_nav) )

            NavigationItem(onClick = {
                navController.navigate(BuyerScreens.ORDERS.name)
                onSelectedChange(BuyerScreens.ORDERS.name)
            },selectedRoute = selectedRoute,
                thisRoute = BuyerScreens.ORDERS.name,
                title = "Orders", imageVector = ImageVector.vectorResource(R.drawable.order_nav) )

            NavigationItem(onClick = {
                navController.navigate(BuyerScreens.CHATS.name)
                onSelectedChange(BuyerScreens.CHATS.name)
            },selectedRoute = selectedRoute,
                thisRoute = BuyerScreens.CHATS.name ,
                title = "Chats", imageVector = ImageVector.vectorResource(R.drawable.chats_nav) )

            NavigationItem(onClick = {
                navController.navigate(BuyerScreens.PROFILE.name)
                onSelectedChange(BuyerScreens.PROFILE.name)
            },selectedRoute = selectedRoute,
                thisRoute = BuyerScreens.PROFILE.name ,title = "Profile", imageVector = ImageVector.vectorResource(
                    R.drawable.profile_nav) )

        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BuyerNavigation(rootNavController: NavHostController) {
    val buyerNavController = rememberNavController()

    var selectedRoute by remember { mutableStateOf(BuyerScreens.MARKET.name) }
    val currentBackStackEntry by buyerNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val systemUiController = rememberSystemUiController()
    val viewModel: BuyerViewModel = hiltViewModel()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Gray
    ) {
        Scaffold(
            bottomBar = { if (
                currentRoute == BuyerScreens.MARKET.name
                || currentRoute == BuyerScreens.ORDERS.name
                || currentRoute == BuyerScreens.CHATS.name
                || currentRoute == BuyerScreens.PROFILE.name) {

                BottomBar(buyerNavController, selectedRoute){selected->
                    selectedRoute = selected

                }
            }}
        ) {
            NavHost(
                navController = buyerNavController,
                startDestination = BuyerScreens.MARKET.name,
                enterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    ExitTransition.None
                },
            ) {
                composable(route = BuyerScreens.MARKET.name) {
                    systemUiController.setStatusBarColor(
                        color = Color(0xff4CAD73), // Replace with your desired color
                    )
                    MarketScreen( buyerNavController, viewModel)
                }
                composable(route = BuyerScreens.ORDERS.name) {

                    OrdersScreen( buyerNavController, viewModel)
                }
                composable(route = BuyerScreens.CHATS.name) {

                    ChatsScreen( buyerNavController, viewModel)
                }
                composable(route = BuyerScreens.CHAT.name) {

                    ChatScreen( buyerNavController, viewModel)
                }

                composable(route = BuyerScreens.PROFILE.name) {

                    ProfileScreen( buyerNavController, viewModel){
                        rootNavController.navigate(Screens.START_SCREEN.name)
                    }
                }

                composable(route = BuyerScreens.PRODUCT_DETAILS.name){

                    ProductDetailsScreen(buyerNavController, viewModel)
                }
                composable(route = BuyerScreens.ORDER_DETAILS.name){

                    OrderDetailsScreen(buyerNavController, viewModel)
                }
                composable(route = BuyerScreens.OFFER_DETAILS.name){

                    OfferDetailsScreen(buyerNavController, viewModel)
                }
                composable(route = BuyerScreens.CART.name){

                    CartScreen(viewModel)
                }
                composable(route = BuyerScreens.NOTIFICATIONS.name){

                    NotificationScreen()
                }




            }
        }
    }
}
