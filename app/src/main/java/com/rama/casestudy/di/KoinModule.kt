package com.rama.casestudy.di

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rama.casestudy.data.remote.retrofit.ApiService
import com.rama.casestudy.data.repository.UserRepositoryImpl
import com.rama.casestudy.domain.repository.UserRepository
import com.rama.casestudy.ui.features.user_list.UserListViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

// Definisikan semua module di satu tempat
private val networkModule = module {
    single {
        Log.d("KoinSetup", ">>> MEMBUAT RETROFIT DENGAN CONVERTER <<<")
        val json = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    single { get<Retrofit>().create(ApiService::class.java) }
}

private val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
}

private val viewModelModule = module {
    viewModel { UserListViewModel(get()) }
}

// Gabungkan semua module menjadi satu list untuk dimuat di Application class
val appModule = listOf(networkModule, repositoryModule, viewModelModule)