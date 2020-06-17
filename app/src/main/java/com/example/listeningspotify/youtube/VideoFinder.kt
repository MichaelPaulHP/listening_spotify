package com.example.listeningspotify.youtube

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class VideoFinder(val q:String): Callback<YouTubeResponse> {

    fun getVideos(): MutableList<Video> {
        try {
            val qEncoded= java.net.URLEncoder.encode(q, "utf-8")
            val videosFound=ServiceBuilder.buildYouTubeService().searchVideos(qEncoded);
            //videosFound.enqueue(this)
            val videos= mutableListOf<Video>()
            GlobalScope.launch {
                val response=videosFound.awaitResponse()
                val resBody=response.body()
                resBody?.items?.forEach {
                    videos.add(Video.fromVideoEntity(it))
                }
            }
            return videos;

        }catch (e:Exception){
            throw e
        }
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     */
    override fun onFailure(call: Call<YouTubeResponse>, t: Throwable) {
        TODO("Not yet implemented")
    }

    /**
     * Invoked for a received HTTP response.
     *
     *
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call [Response.isSuccessful] to determine if the response indicates success.
     */
    override fun onResponse(call: Call<YouTubeResponse>, response: Response<YouTubeResponse>) {
        TODO("Not yet implemented")
    }
}