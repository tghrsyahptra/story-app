package com.tghrsyahptra.storyapp.data.api

import com.tghrsyahptra.storyapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {


    fun getApiService(): ApiService {
        val client = createOkHttpClient()
        val retrofit = createRetrofit(client)
        return retrofit.create(ApiService::class.java)
    }

    /**
     * Membuat dan mengonfigurasi instance [OkHttpClient].
     */
    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * Membuat dan mengonfigurasi instance [Retrofit].
     */
    private fun createRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}