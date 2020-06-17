package com.example.listeningspotify.history


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName="videos")
data class VideoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val thumbnail: String,
    val channelTitle: String

)