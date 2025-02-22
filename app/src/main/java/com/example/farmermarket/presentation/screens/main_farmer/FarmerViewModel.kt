package com.example.farmermarket.presentation.screens.main_farmer

//import java.io.File

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmermarket.common.Constants.BASE_URL
import com.example.farmermarket.common.Constants.userName
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.Conversation
import com.example.farmermarket.data.remote.dto.FarmerDashBoardDto
import com.example.farmermarket.data.remote.dto.SoldProductsDto
import com.example.farmermarket.data.remote.dto.SoldProductsDtoItem
import com.example.farmermarket.data.remote.dto.Message
import com.example.farmermarket.data.remote.dto.OffersListDtoItem
import com.example.farmermarket.data.remote.dto.ProductDTO
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.data.remote.dto.ProductResponseDto
import com.example.farmermarket.domain.usecase.ChangeOfferStatusUseCase
import com.example.farmermarket.domain.usecase.CreateNewChatUseCase
import com.example.farmermarket.domain.usecase.DeleteProductUseCase
import com.example.farmermarket.domain.usecase.GetChatUseCase
import com.example.farmermarket.domain.usecase.GetChatsUseCase
import com.example.farmermarket.domain.usecase.GetFarmerDashBoardUseCase
import com.example.farmermarket.domain.usecase.GetFarmerOffersUseCase
import com.example.farmermarket.domain.usecase.GetProductDetailUseCase
import com.example.farmermarket.domain.usecase.GetProductsUseCase
import com.example.farmermarket.domain.usecase.GetSoldProductsUseCase
import com.example.farmermarket.domain.usecase.GetUsernameByUserIdUseCase
import com.example.farmermarket.domain.usecase.MarkAsReadUseCase
import com.example.farmermarket.domain.usecase.SendMessageUseCase
import com.example.farmermarket.domain.usecase.UpdateProductStatusUseCase
import com.example.farmermarket.presentation.screens.main_buyer.OffersListState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.io.IOException
import javax.inject.Inject


data class ChatListState(
    val isLoading: Boolean = false,
    val chatResponse: List<Conversation> =  emptyList(),
    val error: String = ""
)

data class ProductListState(
    val isLoading: Boolean = false,
    val productResponse: ProductResponseDto =  ProductResponseDto(),
    val error: String = ""
)

data class ProductUpdateState(
    var isLoading: Boolean = false,
    var updated:Boolean? = null,
    var error: String = ""
)
data class ProductAddState(
    var isLoading: Boolean = false,
    var isSent:Boolean? = null,
    var error: String = ""
)

data class OrderedProductListState(
    var isLoading: Boolean? = null,
    var orderedProductList:SoldProductsDto = SoldProductsDto(),
    var error: String = ""
)
data class InTransitProductListState(
    var isLoading: Boolean? = null,
    var inTransitProductList:SoldProductsDto = SoldProductsDto(),
    var error: String = ""
)

data class PackedProductListState(
    var isLoading: Boolean? = null,
    var packedProductList:SoldProductsDto = SoldProductsDto(),
    var error: String = ""
)
data class CompletedProductListState(
    var isLoading: Boolean? = null,
    var completedProductList:SoldProductsDto = SoldProductsDto(),
    var error: String = ""
)
data class CancelledProductListState(
    var isLoading: Boolean? = null,
    var cancelledProductList:SoldProductsDto = SoldProductsDto(),
    var error: String = ""
)

data class UpdateProductStatusState(
    var isLoading: Boolean? = null,
    var isChanged:Boolean? = null,
    var error: String = ""
)

data class ChangeOfferStatusState(
    var isLoading: Boolean? = null,
    var isChanged:Boolean? = null,
    var error: String = ""
)

@HiltViewModel
class FarmerViewModel  @Inject constructor(


    private val sharedPrefs: SharedPreferences,
    private val getChatsUseCase: GetChatsUseCase,
    private val getChatUseCase: GetChatUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val getSoldProductsUseCase: GetSoldProductsUseCase,
    private val updateProductStatusUseCase: UpdateProductStatusUseCase,
    private val createNewChatUseCase: CreateNewChatUseCase,
    private val getFarmerOffersUseCase: GetFarmerOffersUseCase,
    private val changeOfferStatusUseCase: ChangeOfferStatusUseCase,
    private val getUsernameByUserIdUseCase: GetUsernameByUserIdUseCase,
    private val markAsReadUseCase: MarkAsReadUseCase,
    private val getFarmerDashBoardUseCase: GetFarmerDashBoardUseCase

): ViewModel() {


    init {
    }


    val chatListState: MutableState<ChatListState> = mutableStateOf(ChatListState())
    val productListState: MutableState<ProductListState> = mutableStateOf(ProductListState())
    val messageList: MutableState<List<Message>> = mutableStateOf(emptyList())
    val selectedChatId: MutableState<String> = mutableStateOf("")
    val selectedParticipantName: MutableState<String> = mutableStateOf("")
    val imageNames: MutableState<List<String>> = mutableStateOf(emptyList())

    val selectedProduct: MutableState<ProductDetailDto> = mutableStateOf(
        ProductDetailDto()
    )

    val offerListState: MutableState<OffersListState> = mutableStateOf(
        OffersListState()
    )
    val productUpdateState: MutableState<ProductUpdateState> = mutableStateOf(ProductUpdateState())
    val productAddState: MutableState<ProductAddState> = mutableStateOf(ProductAddState())

    val orderedProductListState: MutableState<OrderedProductListState> = mutableStateOf(OrderedProductListState())
    val inTransitProductListState: MutableState<InTransitProductListState> = mutableStateOf(InTransitProductListState())
    val packedProductListState: MutableState<PackedProductListState> = mutableStateOf(
        PackedProductListState()
    )
    val completedProductListState: MutableState<CompletedProductListState> = mutableStateOf(CompletedProductListState())
    val cancelledProductListState: MutableState<CancelledProductListState> = mutableStateOf(CancelledProductListState())

    val selectedOrderDetail: MutableState<SoldProductsDtoItem> = mutableStateOf(
        SoldProductsDtoItem()
    )

    val updateProductStatusState : MutableState<UpdateProductStatusState>  = mutableStateOf(
        UpdateProductStatusState()
    )

    val changeOfferStatusState : MutableState<ChangeOfferStatusState>  = mutableStateOf(
        ChangeOfferStatusState()
    )

    val selectedOffer: MutableState<OffersListDtoItem> = mutableStateOf(OffersListDtoItem())

    val farmerDashBoard: MutableState<FarmerDashBoardDto> = mutableStateOf(FarmerDashBoardDto())

    val showOrderScreen = mutableStateOf(OrdersScreens.OFFERS)

    fun getFarmerDashBoard(){

        getFarmerDashBoardUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    farmerDashBoard.value = result.data!!
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }

                else -> {}
            }
        }.launchIn(viewModelScope)

    }

    fun createNewChat(participant: String,onSuccess:(Conversation)-> Unit){
        createNewChatUseCase(participant, onSuccess)

    }


    fun changeOfferStatus(offerId: Int, accepted: Boolean){
        changeOfferStatusUseCase(offerId, accepted).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    changeOfferStatusState.value = changeOfferStatusState.value.copy(isChanged = true)
                }

                is Resource.Error -> {
                    changeOfferStatusState.value = changeOfferStatusState.value.copy(error = "Something went wrong!")
                }

                is Resource.Loading -> {
                    changeOfferStatusState.value = changeOfferStatusState.value.copy(isLoading = true)
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }


    fun getOffers(){
        getFarmerOffersUseCase().onEach { result ->
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


    fun updateProductStatus(status: String, productId: Int){
        updateProductStatusUseCase(status, productId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    updateProductStatusState.value = updateProductStatusState.value.copy(isChanged = true)
                }

                is Resource.Error -> {
                    updateProductStatusState.value = updateProductStatusState.value.copy(error = "Something went wrong!")
                }

                is Resource.Loading -> {
                    updateProductStatusState.value = updateProductStatusState.value.copy(isLoading = true)
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }


    fun getSoldProducts(status: String){
        getSoldProductsUseCase(status).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("kama", result.data.toString())
                    if (status == "ORDERED"){
                        orderedProductListState.value =
                            result.data?.let { orderedProductListState.value.copy(orderedProductList = it, isLoading = false) }!!
                    }else if(status == "IN TRANSIT"){
                        inTransitProductListState.value =
                            result.data?.let { inTransitProductListState.value.copy( inTransitProductList = it, isLoading = false) }!!
                    }else if(status == "PACKED"){
                        packedProductListState.value =
                            result.data?.let { packedProductListState.value.copy( packedProductList = it, isLoading = false) }!!
                    }else if (status == "COMPLETED"){
                        completedProductListState.value =
                            result.data?.let { completedProductListState.value.copy(completedProductList = it, isLoading = false) }!!

                    }else if (status == "CANCELLED"){
                        cancelledProductListState.value =
                            result.data?.let { cancelledProductListState.value.copy(cancelledProductList = it, isLoading = false) }!!

                    }
                }

                is Resource.Error -> {
                    if (status == "ORDERED"){
                        orderedProductListState.value =
                            orderedProductListState.value.copy(error = "Something went wrong",isLoading = false)
                    }else if (status == "IN TRANSIT"){
                        inTransitProductListState.value =
                            inTransitProductListState.value.copy(error = "Something went wrong",isLoading = false)
                    }else if (status == "PACKED"){
                        packedProductListState.value =
                            packedProductListState.value.copy(error = "Something went wrong",isLoading = false)
                    } else if (status == "COMPLETED"){
                        completedProductListState.value =
                            completedProductListState.value.copy(error = "Something went wrong",isLoading = false)
                    }else if (status == "CANCELLED"){
                        cancelledProductListState.value =
                            cancelledProductListState.value.copy(error = "Something went wrong",isLoading = false)

                    }

                }
                is Resource.Loading -> {
                    if (status == "ORDERED"){
                        orderedProductListState.value =
                            orderedProductListState.value.copy(isLoading = true)
                    }else if (status == "IN TRANSIT"){
                        inTransitProductListState.value =
                            inTransitProductListState.value.copy(isLoading = true)
                    }else if (status == "PACKED"){
                        packedProductListState.value =
                            packedProductListState.value.copy(isLoading = true)
                    }else if (status == "COMPLETED"){
                        completedProductListState.value =
                            completedProductListState.value.copy(isLoading = true)

                    }else if (status == "CANCELLED"){
                        cancelledProductListState.value =
                            cancelledProductListState.value.copy(isLoading = true)
                    }

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

    fun updateProduct(id: Int, product: ProductDTO) {
        productUpdateState.value = productUpdateState.value.copy(isLoading = true)
        productUpdateState.value = productUpdateState.value.copy(updated = false)

        viewModelScope.launch {
            try {
                val client = OkHttpClient()


                val gson = Gson()
                val productDtoJson = gson.toJson(product)

                // Create RequestBody with JSON content type
                val mediaType = "application/json".toMediaType()
                val requestBody = productDtoJson.toRequestBody(mediaType)

                // Ensure product ID is properly inserted into the URL
                val url = "$BASE_URL/api/products/${product.id}/data"

                // Build the request
                val request = Request.Builder()
                    .url(url)
                    .put(requestBody) // Use PUT method with JSON body
                    .addHeader("Authorization", "Bearer ${sharedPrefs.getString("token", "")}")
                    .build()


                // Execute request asynchronously
                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        e.printStackTrace()
                        println("Network Error: ${e.message}")
                        productUpdateState.value =
                            productUpdateState.value.copy(error = "Something went wrong! Try again later")
                        productUpdateState.value = productUpdateState.value.copy(isLoading = false)
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        response.use {
                            if (response.isSuccessful) {
                                getProducts()
//                                getFirstImage(product.id, "0")
                                Log.d("kama", "Request Successful: ${response.body?.string()}")
                                productUpdateState.value = productUpdateState.value.copy(error = "")
                                productUpdateState.value =
                                    productUpdateState.value.copy(isLoading = false)
                                productUpdateState.value =
                                    productUpdateState.value.copy(updated = true)
                            } else {
                                Log.d("kama", "Request Fail: ${response.body?.string()}")
                                productUpdateState.value =
                                    response.body?.string()
                                        ?.let { it1 -> productUpdateState.value.copy(error = it1) }!!
                                productUpdateState.value =
                                    productUpdateState.value.copy(updated = null)
                            }
                        }
                    }
                })
            } catch (e: Exception) {

                productUpdateState.value =
                    productUpdateState.value.copy(error = "Something went wrong! Try again later")
                productUpdateState.value = productUpdateState.value.copy(updated = false)
                e.printStackTrace()

                Log.d("kama", "Request exception: ${e.message}")
            }
        }
    }


    fun deleteProduct(id: Int, onReady: () -> Unit) {
        deleteProductUseCase(id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    onReady()
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }


    fun createProduct(context: Context, images: List<Uri>, productDTO: ProductDTO) {
        productAddState.value = productAddState.value.copy(isLoading = true)
        productAddState.value = productAddState.value.copy(isSent = false)

        viewModelScope.launch {
            try {
                val client = OkHttpClient()

                // Initialize the multipart body builder
                val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

                // Add each image to the multipart body
                for ((index, uri) in images.withIndex()) {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val imageBytes = inputStream?.readBytes()
                    val mimeType = context.contentResolver.getType(uri)
                        ?: "image/jpeg"  // Adjust MIME type based on Uri

                    if (imageBytes != null) {
                        val imageRequestBody =
                            imageBytes.toRequestBody(mimeType.toMediaTypeOrNull())
                        bodyBuilder.addFormDataPart(
                            "images", "image_$index.jpg", imageRequestBody
                        )
                    } else {
                        println("Failed to open input stream for URI: $uri")
                    }
                }

                // Convert productDTO to JSON and add to multipart
                val gson = Gson()
                val productDtoJson = gson.toJson(productDTO)
                bodyBuilder.addFormDataPart(
                    "productDTO",
                    null,
                    productDtoJson.toRequestBody("application/json".toMediaTypeOrNull())
                )

                val body = bodyBuilder.build()

                // Create the request
                val request = Request.Builder()
                    .url("$BASE_URL/api/products")
                    .post(body)
                    .addHeader("Authorization", "Bearer ${sharedPrefs.getString("token", "")}")
                    .build()

                // Execute request asynchronously
                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        e.printStackTrace()
                        println("Network Error: ${e.message}")
                        productAddState.value =
                            productAddState.value.copy(error = "Something went wrong! Try again later")
                        productAddState.value = productAddState.value.copy(isLoading = false)
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        response.use {
                            if (response.isSuccessful) {
                                getProducts()
//                                getFirstImage(productDTO.id, "0")
                                println("Request Successful: ${response.body?.string()}")
                                productAddState.value = productAddState.value.copy(error = "")
                                productAddState.value =
                                    productAddState.value.copy(isLoading = false)
                                productAddState.value = productAddState.value.copy(isSent = true)
                            } else {
                                println("Request Failed: ${response.code} - ${response.body?.string()}")
                                productAddState.value =
                                    productAddState.value.copy(error = "Something went wrong! Try again later")
                                productAddState.value =
                                    productAddState.value.copy(isLoading = false)
                            }
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                println("Exception: ${e.message}")
            }
        }
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

                else -> {}
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

    fun markAsRead(conversationId: String) {
        markAsReadUseCase(conversationId)
    }

    fun getUserName(userId:String, onSuccess: (String) -> Unit) {

        getUsernameByUserIdUseCase(userId, onSuccess)
    }

    fun sendMessage(conversationId: String, message: String, userName: String) {
        sendMessageUseCase(conversationId, message, userName)

    }



    fun connectStomp(productId:Int, onDelete:() -> Unit) {
        val mStompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "http://26.143.23.99:8080/swe-websocket/websocket"
        )

        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000)

        val compositeDisposable = CompositeDisposable()

        // Monitor the connection lifecycle
        val dispLifecycle: Disposable = mStompClient.lifecycle()
            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread()) // To handle logs on the main thread
            .subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> {
                        Log.d("websocket", "Stomp connection opened")

                        // Only subscribe to the topic after the connection is opened
                        subscribeToTopic(mStompClient, compositeDisposable, productId, onDelete)
                    }
                    LifecycleEvent.Type.ERROR -> {
                        Log.e("websocket", "Stomp connection error", lifecycleEvent.exception)
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        Log.d("websocket", "Stomp connection closed")
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                        Log.e("websocket", "Stomp failed server heartbeat")
                    }
                }
            }
        compositeDisposable.add(dispLifecycle)

        // Connect the Stomp client
        mStompClient.connect()
    }

    // Subscribe to the topic
    data class Product(
        val id: Int,
        val name: String,
        val category: String,
        val price: Double,
        val unit: String,
        val quantity: Double,
        val description: String,
        val farmId: Int?, // Nullable because it's null in your JSON
        val createdAt: String,
        val updatedAt: String
    )
    private fun subscribeToTopic(mStompClient: StompClient, compositeDisposable: CompositeDisposable, productId:Int, onDelete:() -> Unit) {
        val dispTopic: Disposable = mStompClient.topic("/topic/products/$productId")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) // Process messages on the main thread
            .subscribe({ topicMessage ->
                if (topicMessage.payload == "delete"){
                    getProducts()
                    onDelete()
                }else{
                    val payload = topicMessage.payload // The JSON string
                    val gson = Gson()

                    try {
                        val product = gson.fromJson(payload, Product::class.java)
                        selectedProduct.value = selectedProduct.value.copy(
                            name = product.name,
                            price = product.price,
                            unit = product.unit,
                            quantity = product.quantity,
                            description = product.description
                        )
                    } catch (e: Exception) {
                        Log.e("websocket", "Error parsing JSON", e)
                    }
                }
                Log.d("websocket", "Received message: ${topicMessage.payload}")


            }, { throwable ->
                Log.e("websocket", "Error on subscribe topic", throwable)
            })
        compositeDisposable.add(dispTopic)
    }

}


