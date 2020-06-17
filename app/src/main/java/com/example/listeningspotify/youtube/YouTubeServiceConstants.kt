package com.example.listeningspotify.youtube

import com.example.listeningspotify.config.YouTubeCredentials

object YouTubeServiceConstants {
    const val URL_BASE="https://www.googleapis.com/youtube/v3/"
    const val SEARCH_VIDEOS="search?part=snippet&type=videos&key=${YouTubeCredentials.API_KEY}";
}