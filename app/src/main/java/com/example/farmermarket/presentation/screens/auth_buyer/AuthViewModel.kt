package com.example.farmermarket.presentation.screens.auth_buyer

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmermarket.common.Constants
import com.example.farmermarket.common.Resource
import com.example.farmermarket.data.remote.dto.LoginDto
import com.example.farmermarket.data.remote.dto.SignupDto
import com.example.farmermarket.domain.usecase.AddUserToFirebaseUseCase
import com.example.farmermarket.domain.usecase.GetCurrentUserUseCase
import com.example.farmermarket.domain.usecase.LoginUseCase
import com.example.farmermarket.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import javax.inject.Inject


data class SignUpState(
    var isSuccessfull: Boolean? = null,
    var incorrectPasswordLength: Boolean? = null,
    var userAlreadyExists: Boolean? = null,
    var internalServerError: Boolean? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(

    private val signUpUseCase: SignUpUseCase,
    private val loginUseCase: LoginUseCase,
    private val sharedPreferences: SharedPreferences,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val addUserToFirebaseUseCase: AddUserToFirebaseUseCase

): ViewModel() {

    val signUpState: MutableState<SignUpState> = mutableStateOf(SignUpState())



    fun signUp(signupDto: SignupDto, onSuccess: ()-> Unit, ){
        signUpUseCase(signupDto).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data?.isSuccessful == true) {

                        onSuccess()
                    }else if(result.data?.code().toString() == "400"){
                        signUpState.value = signUpState.value.copy(userAlreadyExists = true)

                    }else if(result.data?.code().toString() == "422"){
                        signUpState.value = signUpState.value.copy(incorrectPasswordLength = true)

                    }else if(result.data?.code().toString() == "500"){
                        signUpState.value = signUpState.value.copy(internalServerError = true)
                    }

                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun login(loginDto: LoginDto, onSuccess: ()-> Unit,onFailure: ()-> Unit ){
        loginUseCase(loginDto).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data?.access_token?.isNotEmpty() == true) {
                        onSuccess()
                        Log.i("KAMA", result.data.access_token)
                        saveToPreferences("token", result.data.access_token)
                        getCurrUser()
                    }else{
                        onFailure()
                    }

                }

                is Resource.Error -> {
                    onFailure()
                }

                is Resource.Loading -> {

                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun getCurrUser(){
        getCurrentUserUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { addUserToFirebaseUseCase(it.uuid, result.data.first_name + " "+ result.data.last_name) }
                    if (result.data != null){
                        saveToPreferences("uuid", result.data.uuid)
                        Constants.uuid = result.data.uuid
                        Constants.userName = result.data.first_name
                        Constants.phone = result.data.phone
                        Constants.email = result.data.email
                        Constants.surname = result.data.last_name
                    }else{
                        saveToPreferences("uuid", "default")
                    }

                    result.data?.let { Log.i("KAMA", it.uuid) }

                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }


    fun saveToPreferences(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getFromPreferences(key: String): String? {
        sharedPreferences.getString(key, "")?.let { Log.i("KAMA", it) }
        return sharedPreferences.getString(key, "")
    }

    fun addUserToFireBase(userId: String, userName: String){

        addUserToFirebaseUseCase(userId, userName)
    }




}