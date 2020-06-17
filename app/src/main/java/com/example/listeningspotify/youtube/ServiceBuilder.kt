package com.example.listeningspotify.youtube

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceBuilder {

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(YouTubeServiceConstants.URL_BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun buildYouTubeService(): YouTubeService {
        return retrofit.create(YouTubeService::class.java)
    }
}