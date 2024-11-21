package com.example.farmermarket.presentation.screens.main_farmer

//import java.io.File

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmermarket.common.Constants.BASE_URL
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.Conversation
import com.example.farmermarket.data.remote.dto.Message
import com.example.farmermarket.data.remote.dto.ProductDTO
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.data.remote.dto.ProductResponseDto
import com.example.farmermarket.domain.usecase.DeleteProductUseCase
import com.example.farmermarket.domain.usecase.GetChatUseCase
import com.example.farmermarket.domain.usecase.GetChatsUseCase
import com.example.farmermarket.domain.usecase.GetProductDetailUseCase
import com.example.farmermarket.domain.usecase.GetProductsUseCase
import com.example.farmermarket.domain.usecase.GetRealTimeMessagesUseCase
import com.example.farmermarket.domain.usecase.SendMessageUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.utils.EmptyContent.headers
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
import ua.naiksoftware.stomp.dto.StompHeader
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

@HiltViewModel
class FarmerViewModel  @Inject constructor(


    private val sharedPrefs: SharedPreferences,
    private val getChatsUseCase: GetChatsUseCase,
    private val getChatUseCase: GetChatUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getRealTimeMessagesUseCase: GetRealTimeMessagesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getProductDetailUseCase: GetProductDetailUseCase

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


    val productUpdateState: MutableState<ProductUpdateState> = mutableStateOf(ProductUpdateState())
    val productAddState: MutableState<ProductAddState> = mutableStateOf(ProductAddState())


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
                                    productUpdateState.value.copy(error = "Something went wrong! Try again later")
                                productUpdateState.value =
                                    productUpdateState.value.copy(updated = false)
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

    fun getChat(conversationId: String) {

        getChatUseCase(conversationId) { messages ->
            messageList.value = messages
            Log.i("MESSAGE", messages.toString())
        }
    }

    fun sendMessage(conversationId: String, message: String, userName: String) {
        sendMessageUseCase(conversationId, message, userName)

    }


    fun getRealTimeMessages() {
        viewModelScope.launch {
            getRealTimeMessagesUseCase { message ->
                // Process the received message
                Log.d("kama", message)
                getProducts()
            }
        }
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


