package com.gitlab.nastyaka.chat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChatsEntity::class], version = 1)
abstract class ChatsDB : RoomDatabase() {
    abstract fun chatsDao(): ChatsDAO

    companion object {

        @Volatile
        private var INSTANCE: ChatsDB? = null

        fun getDatabase(context: Context): ChatsDB {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): ChatsDB {
            return Room.databaseBuilder(
                context.applicationContext,
                ChatsDB::class.java,
                "ChatsDB"
            ).build()
        }
    }
}