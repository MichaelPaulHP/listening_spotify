package com.example.listeningspotify.history

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [VideoEntity::class], version = 1)
abstract class MyRoomDataBase:RoomDatabase() {
    abstract fun videoDao():VideoDao

    companion object{
        @Volatile
        private var INSTANCE: MyRoomDataBase? = null

        fun getDatabase(context: Context): MyRoomDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyRoomDataBase::class.java,
                    "my_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}