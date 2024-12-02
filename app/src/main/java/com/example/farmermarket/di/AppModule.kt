package com.example.farmermarket.di

import android.content.Context
import android.content.SharedPreferences
import com.example.farmermarket.common.Constants
import com.example.farmermarket.data.remote.AuthApi
import com.example.farmermarket.data.remote.FarmApi
import com.example.farmermarket.data.repository.AuthRepositoryImpl
//import com.example.farmermarket.data.remote.GoogleBooksApi
//import com.example.farmermarket.data.repository.BookRepositoryImpl
import com.example.farmermarket.data.repository.FarmRepositoryImpl
import com.example.farmermarket.data.repository.FirebaseRepository
import com.example.farmermarket.data.repository.WebSocketServiceImpl
import com.example.farmermarket.domain.repository.AuthRepository
import com.example.farmermarket.domain.repository.FarmRepository
import com.example.farmermarket.domain.usecase.AddToCartUseCase
import com.example.farmermarket.domain.usecase.AddUserToFirebaseUseCase
import com.example.farmermarket.domain.usecase.CreateNewChatUseCase
import com.example.farmermarket.domain.usecase.GetCartUseCase
import com.example.farmermarket.domain.usecase.GetChatUseCase
import com.example.farmermarket.domain.usecase.GetChatsUseCase
import com.example.farmermarket.domain.usecase.GetRealTimeMessagesUseCase
import com.example.farmermarket.domain.usecase.GetUsernameByUserIdUseCase
import com.example.farmermarket.domain.usecase.MarkAsReadUseCase
import com.example.farmermarket.domain.usecase.SendMessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


//    @Provides
//    @Singleton
//    fun provideBookApi(): GoogleBooksApi {
//        return Retrofit.Builder()
//            .baseUrl("https://www.googleapis.com/books/v1/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(GoogleBooksApi::class.java)
//    }
@Provides
@Singleton
fun provideFarmApi(): FarmApi {
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FarmApi::class.java)
}

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return Retrofit.Builder()
            .baseUrl(Constants.AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }
    @Provides
    @Singleton
    fun provideFarmRepository(api: FarmApi, sharedPrefs: SharedPreferences): FarmRepository {
        return FarmRepositoryImpl(sharedPrefs,api)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi, sharedPrefs: SharedPreferences): AuthRepository {
        return AuthRepositoryImpl(api,sharedPrefs)
    }

    @Provides
    fun provideGetUsernameByUserIdUseCase(): GetUsernameByUserIdUseCase {
        return GetUsernameByUserIdUseCase()
    }

    @Provides
    fun provideGetChatsUseCase(): GetChatsUseCase {
        return GetChatsUseCase()
    }
    @Provides
    fun provideGetChatUseCase(): GetChatUseCase {
        return GetChatUseCase()
    }

    @Provides
    fun provideSendMessageUseCase(): SendMessageUseCase {
        return SendMessageUseCase()

    }

    @Provides
    fun provideMarkAsUseCase(): MarkAsReadUseCase {
        return MarkAsReadUseCase()

    }

    @Provides
    fun provideAddToCarUseCase(): AddToCartUseCase {
        return AddToCartUseCase()

    }

    @Provides
    fun provideAddUserToFirebaseUseCase(): AddUserToFirebaseUseCase {
        return AddUserToFirebaseUseCase()

    }

    @Provides
    fun provideGetCartUseCase(): GetCartUseCase {
        return GetCartUseCase()

    }

    @Provides
    fun provideCreateNewChatUseCase(): CreateNewChatUseCase {
        return CreateNewChatUseCase()
    }

    @Provides
    fun provideWebSocketService(): WebSocketServiceImpl {
        // Provide any required dependencies if needed
        return WebSocketServiceImpl(/* dependencies */)
    }

    @Provides
    fun provideGetRealTimeMessagesUseCase(webSocketService: WebSocketServiceImpl): GetRealTimeMessagesUseCase {
        return GetRealTimeMessagesUseCase(webSocketService)
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(): FirebaseRepository {
        return FirebaseRepository()
    }

    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }
}