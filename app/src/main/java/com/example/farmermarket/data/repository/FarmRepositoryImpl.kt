package com.example.farmermarket.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.farmermarket.data.remote.FarmApi
import com.example.farmermarket.data.remote.dto.BuyerDashBoardDto
import com.example.farmermarket.data.remote.dto.CreateOfferDto
import com.example.farmermarket.data.remote.dto.CreateOrderDto
import com.example.farmermarket.data.remote.dto.FarmerDashBoardDto
import com.example.farmermarket.data.remote.dto.OffersListDto
import com.example.farmermarket.data.remote.dto.SoldProductsDto
import com.example.farmermarket.data.remote.dto.OrdersResponseDto
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.data.remote.dto.ProductResponseDto
import com.example.farmermarket.domain.repository.FarmRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject


class FarmRepositoryImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences,
    private val api: FarmApi): FarmRepository

{

    override suspend fun getProducts(): ProductResponseDto {
        Log.d("kama","Bearer ${sharedPrefs.getString("token", "")}")
        return api.getProducts("Bearer ${sharedPrefs.getString("token", "")}")
    }


    override suspend fun deleteProduct(id: Int): Response<ResponseBody> {
        return api.deleteProduct("Bearer ${sharedPrefs.getString("token", "")}",id)
    }

    override suspend fun getProduct(id: Int): ProductDetailDto {
        return api.getProduct("Bearer ${sharedPrefs.getString("token", "")}", id)
    }

    override suspend fun createOrder(createOrderDto: CreateOrderDto): Response<ResponseBody> {
        return api.createOrder("Bearer ${sharedPrefs.getString("token", "")}", createOrderDto)
    }

    override suspend fun getOrders(): OrdersResponseDto {
        return api.getOrders("Bearer ${sharedPrefs.getString("token", "")}", sharedPrefs.getString("uuid", "") )
    }

    override suspend fun getSoldProducts(status: String): SoldProductsDto {
        return  api.getSoldProducts("Bearer ${sharedPrefs.getString("token", "")}", sharedPrefs.getString("uuid", ""), status )
    }

    override suspend fun changeProductStatus(status: String, productId: Int): Response<ResponseBody> {
        return api.changeProductStatus("Bearer ${sharedPrefs.getString("token", "")}", sharedPrefs.getString("uuid", ""),productId, status )
    }

    override suspend fun getBuyerOffers(): OffersListDto {
        return api.getBuyerOffers("Bearer ${sharedPrefs.getString("token", "")}", sharedPrefs.getString("uuid", ""))
    }

    override suspend fun getFarmerOffers(): OffersListDto {
        return api.getFarmerOffers("Bearer ${sharedPrefs.getString("token", "")}", sharedPrefs.getString("uuid", ""))
    }

    override suspend fun createOffer(createOfferDto: CreateOfferDto): Response<ResponseBody> {
        return api.createOffer("Bearer ${sharedPrefs.getString("token", "")}", createOfferDto)
    }

    override suspend fun changeOfferStatus(
        offerId: Int,
        accepted: Boolean
    ): Response<ResponseBody> {
        return api.changeOfferStatus("Bearer ${sharedPrefs.getString("token", "")}", offerId, accepted)

    }

    override suspend fun getFarmerDashBoard(): FarmerDashBoardDto {
        return api.getFarmerDashBoard("Bearer ${sharedPrefs.getString("token", "")}", sharedPrefs.getString("uuid", ""))
    }

    override suspend fun getBuyerDashBoard(): BuyerDashBoardDto {
        return api.getBuyerDashBoard("Bearer ${sharedPrefs.getString("token", "")}", sharedPrefs.getString("uuid", ""))
    }


}