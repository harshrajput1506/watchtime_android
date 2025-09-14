package com.app.core.network.di

import com.app.core.network.api.TMDBApiService
import com.app.core.utils.constants.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val networkModule = module {

    // JSON configuration
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }
    }


    // OkHttp Client
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit instance
    single {
        Retrofit.Builder()
            .baseUrl(Constants.TMDB_BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    // TMDB API Service
    single<TMDBApiService> {
        get<Retrofit>().create(TMDBApiService::class.java)
    }
}
