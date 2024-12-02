package com.example.farmermarket.data.remote

import BuyerScreens
import com.example.farmermarket.data.remote.dto.BuyerDashBoardDto
import com.example.farmermarket.data.remote.dto.CreateOfferDto
import com.example.farmermarket.data.remote.dto.CreateOrderDto
import com.example.farmermarket.data.remote.dto.FarmerDashBoardDto
import com.example.farmermarket.data.remote.dto.OffersListDto
import com.example.farmermarket.data.remote.dto.SoldProductsDto
import com.example.farmermarket.data.remote.dto.OrdersResponseDto
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.data.remote.dto.ProductResponseDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface FarmApi {

    @POST("/api/products")
    suspend fun getProducts(
        @Header("Authorization") current_user: String,
        @Body body: Unit = Unit ,
    ): ProductResponseDto


    @GET("/api/products/{id}")
    suspend fun getProduct(
        @Header("Authorization") current_user: String,
        @Path("id") id: Int): ProductDetailDto


    @DELETE("api/products/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") current_user: String,
        @Path("id") id: Int): Response<ResponseBody>


    @POST("/api/orders")
    suspend fun createOrder(
        @Header("Authorization") current_user: String,
        @Body createOrderDto: CreateOrderDto,
    ): Response<ResponseBody>

    @GET("/api/orders")
    suspend fun getOrders(
        @Header("Authorization") current_user: String,
        @Query("buyerId") buyerId: String?,
    ): OrdersResponseDto


    @GET("/api/farms/{farmId}/sold-products")
    suspend fun getSoldProducts(
        @Header("Authorization") current_user: String,
        @Path("farmId") farmId: String?,
        @Query("status") status: String,
    ): SoldProductsDto

    @PATCH("/api/farms/{farmId}/sold-products/{productId}/status")
    suspend fun changeProductStatus(
        @Header("Authorization") current_user: String,
        @Path("farmId") farmId: String?,
        @Path("productId") productId: Int,
        @Query("status") status: String
    ): Response<ResponseBody>


    @GET("/api/offers")
    suspend fun getBuyerOffers(
        @Header("Authorization") current_user: String,
        @Query("userId") userId: String?
    ):OffersListDto

    @GET("/api/offers")
    suspend fun getFarmerOffers(
        @Header("Authorization") current_user: String,
        @Query("farmId") farmId: String?
    ):OffersListDto

    @POST("/api/offers")
    suspend fun createOffer(
        @Header("Authorization") current_user: String,
        @Body createOfferDto: CreateOfferDto
    ):Response<ResponseBody>

    @PUT("/api/offers/{offerId}")
    @Headers("Content-Type: application/json")
    suspend fun changeOfferStatus(
        @Header("Authorization") current_user: String,
        @Path("offerId") offerId: Int,
        @Query("isAccepted") accept: Boolean
    ):Response<ResponseBody>


    @GET("/api/dashboards/farmer")
    suspend fun getFarmerDashBoard(
        @Header("Authorization") current_user: String,
        @Query("farmerId") farmId: String?
    ):FarmerDashBoardDto

    @GET("/api/dashboards/buyer")
    suspend fun getBuyerDashBoard(
        @Header("Authorization") current_user: String,
        @Query("buyerId") buyerId: String?
    ):BuyerDashBoardDto













}