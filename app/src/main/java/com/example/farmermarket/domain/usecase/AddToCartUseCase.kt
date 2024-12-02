package com.example.farmermarket.domain.usecase

import com.example.farmermarket.data.remote.dto.ProductDetailDto
import com.example.farmermarket.data.repository.FirebaseRepository

class AddToCartUseCase {

    operator fun invoke(userId: String, productDetailDto: ProductDetailDto, onSuccess: ()->Unit, onFailure: (Exception)-> Unit){
        val repo = FirebaseRepository()
        repo.saveProductDetail(userId,productDetailDto, onSuccess, onFailure)

    }
}