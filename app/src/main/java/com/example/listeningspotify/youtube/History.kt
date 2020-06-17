package com.example.listeningspotify.youtube

class History {
    private val myHistory= mutableListOf<Video>()
    fun addVideo(video: Video){
        if(video !in myHistory ){
            myHistory.add(video)
        }
    }
    fun getHistory(): MutableList<Video> {
        return this.myHistory;
    }
}
