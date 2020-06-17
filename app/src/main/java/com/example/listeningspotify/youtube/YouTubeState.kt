package com.example.listeningspotify.youtube

abstract class YouTubeState {}

class YouTubeSearching:YouTubeState(){}
class YouTubeVideosFound(val List:List<Video>):YouTubeState(){}
class YouTubeFailure(val message:String):YouTubeState(){}