package com.example.farmermarket.presentation.screens.main_buyer

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.BuyerDashBoardDto
import com.example.farmermarket.data.remote.dto.Conversation
import com.example.farmermarket.data.remote.dto.CreateOfferDto
import com.example.farmermarket.data.remote.dto.CreateOrderDto
import com.example.farmermarket.data.remote.dto.FarmerDashBoardDto
import com.example.farmermarket.data.remote.dto.Message
import com.example.farmermarket.data.remote.dto.OffersListDto
import com.example.farmermarket.data.remote.dto.OffersListDtoItem
import com.example.farmermarket.data.remote.dto.OrdersResponseDto
import com.example.farmermarket.data.remote.dto.OrdersResponseDtoItem
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.domain.usecase.AddToCartUseCase
import com.example.farmermarket.domain.usecase.CreateNewChatUseCase
import com.example.farmermarket.domain.usecase.CreateOfferUseCase
import com.example.farmermarket.domain.usecase.CreateOrderUseCase
import com.example.farmermarket.domain.usecase.GetBuyerDashBoardUseCase
import com.example.farmermarket.domain.usecase.GetChatUseCase
import com.example.farmermarket.domain.usecase.GetChatsUseCase
import com.example.farmermarket.domain.usecase.GetBuyerOffersUseCase
import com.example.farmermarket.domain.usecase.GetCartUseCase
import com.example.farmermarket.domain.usecase.GetOrdersUseCase
import com.example.farmermarket.domain.usecase.GetProductDetailUseCase
import com.example.farmermarket.domain.usecase.GetProductsUseCase
import com.example.farmermarket.domain.usecase.MarkAsReadUseCase
import com.example.farmermarket.domain.usecase.SendMessageUseCase
import com.example.farmermarket.presentation.screens.main_farmer.ChatListState
import com.example.farmermarket.presentation.screens.main_farmer.ProductListState
import com.example.farmermarket.presentation.screens.main_farmer.getParticipantName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderCreationState(
    var isLoading: Boolean = false,
    var created:Boolean? = null,
    var error: String = ""
)

data class CartListState(
    val isLoading: Boolean = false,
    val cartResponse: List<ProductDetailDto> =  emptyList(),
    val error: String = ""
)
data class OfferCreationState(
    var isLoading: Boolean = false,
    var created:Boolean? = null,
    var error: String = ""
)
data class OrderListState(
    var isLoading: Boolean = false,
    var data:OrdersResponseDto = OrdersResponseDto(),
    var error: String = ""
)

data class OffersListState(
    var isLoading: Boolean = false,
    var data:OffersListDto = OffersListDto(),
    var error: String = ""
)
@HiltViewModel
class BuyerViewModel  @Inject constructor(

    private val getChatsUseCase: GetChatsUseCase,
    private val getChatUseCase: GetChatUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val getOrdersUseCase: GetOrdersUseCase,
    private val createNewChatUseCase: CreateNewChatUseCase,
    private val getBuyerOffersUseCase: GetBuyerOffersUseCase,
    private val createOfferUseCase: CreateOfferUseCase,
    private val markAsReadUseCase: MarkAsReadUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val getBuyerDashBoardUseCase: GetBuyerDashBoardUseCase

): ViewModel() {


    val chatListState: MutableState<ChatListState> = mutableStateOf(ChatListState())
    val cartListState: MutableState<CartListState> = mutableStateOf(CartListState())
    val messageList: MutableState<List<Message>> = mutableStateOf(emptyList())
    val selectedChatId: MutableState<String> = mutableStateOf("")
    val selectedParticipantName: MutableState<String> = mutableStateOf("")
    val productListState: MutableState<ProductListState> = mutableStateOf(ProductListState())
    val selectedProduct: MutableState<ProductDetailDto> = mutableStateOf(
        ProductDetailDto()
    )
    val orderCreationState: MutableState<OrderCreationState> = mutableStateOf(
        OrderCreationState()
    )
    val offerCreationState: MutableState<OfferCreationState> = mutableStateOf(
        OfferCreationState()
    )
    val orderListState: MutableState<OrderListState> = mutableStateOf(
        OrderListState()
    )

    val offerListState: MutableState<OffersListState> = mutableStateOf(
        OffersListState()
    )

    val selectedOrder: MutableState<OrdersResponseDtoItem> = mutableStateOf(OrdersResponseDtoItem())

    val selectedOffer: MutableState<OffersListDtoItem> = mutableStateOf(OffersListDtoItem())

    val showOrderScreen = mutableStateOf(OrdersScreens.OFFERS)

    val buyerDashBoard: MutableState<BuyerDashBoardDto> = mutableStateOf(BuyerDashBoardDto())

    init {
        viewModelScope.launch {

        }
    }


    fun addToCart(userId: String,productDetailDto: ProductDetailDto, onSuccess: () -> Unit, onFailure:(Exception)->Unit){
        addToCartUseCase(userId,productDetailDto, onSuccess, onFailure)
    }

    fun markAsRead(conversationId: String) {
        markAsReadUseCase(conversationId)
    }


    fun getOffers(){
        getBuyerOffersUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    offerListState.value = result.data?.let { OffersListState(data = it) }!!
                }

                is Resource.Error -> {
                    offerListState.value = OffersListState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    offerListState.value = OffersListState(
                        isLoading = true
                    )
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun createNewChat(participant: String,onSuccess:(Conversation)-> Unit){
        createNewChatUseCase(participant, onSuccess)

    }



        fun getOrders() {
            getOrdersUseCase().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        orderListState.value = result.data?.let { OrderListState(data = it) }!!
                    }

                    is Resource.Error -> {
                        orderListState.value = OrderListState(
                            error = result.message ?: "An unexpected error occured"
                        )
                    }

                    is Resource.Loading -> {
                        orderListState.value = OrderListState(
                            isLoading = true
                        )
                    }
                    else -> {}
                }
            }.launchIn(viewModelScope)
    }
    fun createOffer(createOfferDto: CreateOfferDto){

        createOfferUseCase(createOfferDto).onEach { result ->
            Log.d("kama", result.message.toString())
            when (result) {

                is Resource.Success -> {
                    offerCreationState.value =
                        result.data?.let { OfferCreationState(created = true) }!!
                    Log.d("kama", result.data.toString())


                }

                is Resource.Error -> {
                    offerCreationState.value = OfferCreationState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    Log.d("kama", "error")


                }

                is Resource.Loading -> {
                    offerCreationState.value = OfferCreationState(isLoading = true)
                    Log.d("kama", "loading")


                }
                else -> {}
            }

        }.launchIn(viewModelScope)
    }

    fun getBuyerDashBoard(){
        getBuyerDashBoardUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    buyerDashBoard.value = result.data!!
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }

                else -> {}
            }
        }.launchIn(viewModelScope)

    }

    fun createOrder(createOrderDto: CreateOrderDto){

        createOrderUseCase(createOrderDto).onEach { result ->
            Log.d("kama", result.message.toString())
            when (result) {

                is Resource.Success -> {
                    orderCreationState.value =
                        result.data?.let { OrderCreationState(created = true) }!!
                    Log.d("kama", result.data.toString())


                }

                is Resource.Error -> {
                    orderCreationState.value = OrderCreationState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    Log.d("kama", "error")


                }

                is Resource.Loading -> {
                    orderCreationState.value = OrderCreationState(isLoading = true)
                    Log.d("kama", "loading")


                }
                else -> {}
            }

    }.launchIn(viewModelScope)
    }

    fun getProduct(id: Int) {
        getProductDetailUseCase(id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    selectedProduct.value = result.data!!
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun getProducts() {
        getProductsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    productListState.value =
                        result.data?.let { ProductListState(productResponse = it) }!!
                    Log.d("REQUEST", result.data.toString())
                }

                is Resource.Error -> {
                    productListState.value = ProductListState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    Log.d("REQUEST", "error")

                }

                is Resource.Loading -> {
                    productListState.value = ProductListState(isLoading = true)
                    Log.d("REQUEST", "loading")

                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }
    fun getCart(userId: String) {

        getCartUseCase(userId) { products ->
            cartListState.value = CartListState(cartResponse = products)
        }.onEach { result ->
            when (result) {
                is Resource.Success -> {
                }

                is Resource.Error -> {


                }

                is Resource.Loading -> {
                    cartListState.value = CartListState(isLoading = true)
                    Log.d("LAB", "loading")

                }
            }
        }.launchIn(viewModelScope)


    }


    fun getChats(user: String) {

        getChatsUseCase(user) { chatRooms ->

            chatListState.value = ChatListState(chatResponse = chatRooms)
        }.onEach { result ->
            when (result) {
                is Resource.Success -> {
                    chatListState.value =
                        result.data?.let { ChatListState(chatResponse = it) }!!
                    Log.d("LAB", result.data.toString())
                }

                is Resource.Error -> {
                    chatListState.value = ChatListState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    Log.d("LAB", "error")

                }

                is Resource.Loading -> {
                    chatListState.value = ChatListState(isLoading = true)
                    Log.d("LAB", "loading")

                }
            }
        }.launchIn(viewModelScope)


    }


    fun getChat(conversation: Conversation, userId: String) {

        selectedParticipantName.value = getParticipantName(conversation.participants, userId)
        getChatUseCase(conversation.id) { messages ->
            messageList.value = messages
            Log.i("MESSAGE", messages.toString())
        }
    }

    fun sendMessage(conversationId: String, message: String, userName: String) {
        sendMessageUseCase(conversationId, message, userName)

    }
}

