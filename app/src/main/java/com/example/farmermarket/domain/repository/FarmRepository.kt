package com.example.farmermarket.domain.repository


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


interface FarmRepository{

    suspend fun getProducts(): ProductResponseDto

    suspend fun deleteProduct(id: Int): Response<ResponseBody>

    suspend fun getProduct(id:Int): ProductDetailDto

    suspend fun createOrder(createOrderDto: CreateOrderDto): Response<ResponseBody>

    suspend fun getOrders(): OrdersResponseDto

    suspend fun getSoldProducts(status: String): SoldProductsDto

    suspend fun changeProductStatus(status: String, productId: Int):Response<ResponseBody>


    suspend fun getBuyerOffers(): OffersListDto

    suspend fun getFarmerOffers(): OffersListDto

    suspend fun createOffer(createOfferDto: CreateOfferDto): Response<ResponseBody>

    suspend fun changeOfferStatus(offerId: Int, accepted: Boolean):Response<ResponseBody>

    suspend fun getFarmerDashBoard(): FarmerDashBoardDto

    suspend fun getBuyerDashBoard(): BuyerDashBoardDto

}