package com.example.farmermarket.domain.repository


import com.example.farmermarket.data.remote.dto.CreatedProductResponseDto
import com.example.farmermarket.data.remote.dto.ProductDTO
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.data.remote.dto.ProductResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Path


interface FarmRepository{

    suspend fun getProducts(): ProductResponseDto

    suspend fun deleteProduct(id: Int): Response<ResponseBody>

    suspend fun getProduct(id:Int): ProductDetailDto


}