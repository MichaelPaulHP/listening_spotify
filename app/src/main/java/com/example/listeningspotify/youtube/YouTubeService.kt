package com.example.listeningspotify.youtube


import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeService{
    

    @GET(YouTubeServiceConstants.SEARCH_VIDEOS)
    fun searchVideos(@Query("q") video: String): Call<YouTubeResponse>

}