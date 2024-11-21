package com.example.farmermarket.data.repository

import android.util.Log
import com.example.farmermarket.domain.repository.WebSocketService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpStatusCode
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.runBlocking
import java.net.ConnectException
import java.net.SocketTimeoutException

class WebSocketServiceImpl : WebSocketService {
        private val client = HttpClient(CIO) {
            install(WebSockets)
        }

        override suspend fun connect(onMessageReceived: (String) -> Unit) {
            try {
                client.webSocket("ws://192.168.137.1:8001/ws") {
                    Log.d("kama connection to websocket:", "")
                    for (message in incoming) {
                        when (message) {
                            is Frame.Text -> onMessageReceived(message.readText())
                            else -> {}
                        }
                    }
                }
            } catch (e: ConnectException) {
                // Handle connection error
                e.message?.let { Log.d("kama Connection refused:", it) }
            } catch (e: SocketTimeoutException) {
                // Handle timeout
                e.message?.let { Log.d("kama Connection refused:", it) }
            } catch (e: ClientRequestException) {
                // Handle HTTP status errors (e.g., 404, 500)
                if (e.response.status == HttpStatusCode.NotFound) {
                    e.message?.let { Log.d("kama Connection refused:", it) }
                } else {
                    e.message?.let { Log.d("kama Connection refused:", it) }
                }
            } catch (e: Exception) {
                // Catch any other exceptions
                e.message?.let { Log.d("kama Connection refused:", it) }
            } finally {
                client.close()
            }
        }

//    override suspend fun connect(onMessageReceived: (String) -> Unit) {
//        client.webSocket("ws://your-server-address/ws") {
////            send("Hello from Android!")
//            for (message in incoming) {
//                when (message) {
//                    is Frame.Text ->onMessageReceived(message.readText())
//
//                    else -> {}
//                }
//            }
//        }
//    }

//    override suspend fun connect(onMessageReceived: (String) -> Unit) {
//        TODO("Not yet implemented")
//    }


}