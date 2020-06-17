package com.example.listeningspotify.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos")
    fun getAll(): LiveData<List<VideoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend  fun insert(vararg videos:VideoEntity)

    @Delete
    suspend  fun delete(video:VideoEntity)

}