package com.example.listeningspotify.youtube

import com.google.gson.annotations.SerializedName

data class id(val videoId:String)
data class snippet(
    val title:String,
    val channelTitle:String,
    val thumbnails:thumbnails
)
data class default(val url:String)

data class thumbnails(val medium:default)
data class VideoEntity(
    val id: id,
    val snippet:snippet

    /*@SerializedName("id.videoId")
    val id:String,
    @SerializedName("snippet.title")
    val title:String,
    @SerializedName("snippet.thumbnails.default")
    val thumbnail:String,
    @SerializedName("snippet.channelTitle")
    val channelTitle: String*/
)
data class YouTubeResponse(
    @SerializedName("items")
    val items:List<VideoEntity>
)