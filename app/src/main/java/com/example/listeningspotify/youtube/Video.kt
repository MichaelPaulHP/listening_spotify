package com.example.listeningspotify.youtube

class Video(

    val id: String,
    val title: String,
    val thumbnail: String,
    val channelTitle: String

) {
    companion object{

        fun fromVideoEntity(entity:VideoEntity): Video {
            val id=entity.id.videoId;
            val title=entity.snippet.title;
            val thumbnail=entity.snippet.thumbnails.medium.url
            val channelTitle=entity.snippet.channelTitle;
            return Video(id,title,thumbnail,channelTitle);
        }
        fun toVideoEntityDB(video: Video): com.example.listeningspotify.history.VideoEntity {
            val id=video.id
            val title=video.title;
            val thumbnail=video.thumbnail
            val channelTitle=video.channelTitle;
            return com.example.listeningspotify.history.VideoEntity(id,title,thumbnail,channelTitle);
        }
    }
}