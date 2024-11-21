package com.example.farmermarket.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.farmermarket.data.remote.FarmApi
import com.example.farmermarket.data.remote.dto.CreatedProductResponseDto
import com.example.farmermarket.data.remote.dto.ProductDTO
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.data.remote.dto.ProductResponseDto
import com.example.farmermarket.domain.repository.FarmRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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


}