package com.example.listeningspotify.youtube

abstract class YouTubeEvent {
}
class YouTubeWatch(video: Video):YouTubeEvent(){}
class YouTubeChromechast(video: Video):YouTubeEvent(){}