package com.gitlab.nastyaka.chat.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChatsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(msg: ChatsEntity)

    @Query("SELECT * FROM chatsentity")
    fun getAll(): List<ChatsEntity>
}