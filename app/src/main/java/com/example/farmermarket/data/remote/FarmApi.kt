package com.example.farmermarket.data.remote

import com.example.farmermarket.data.remote.dto.CreatedProductResponseDto
import com.example.farmermarket.data.remote.dto.ProductDTO
import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.data.remote.dto.ProductResponseDto
import com.example.farmermarket.presentation.screens.main_farmer.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
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











}