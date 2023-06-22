package com.gitlab.nastyaka.chat.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gitlab.nastyaka.chat.Messages

@Dao
interface MessagesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(msg: MessagesEntity)

    @Query("SELECT * FROM MessagesDB")
    fun getAll(): List<MessagesEntity>

    @Query("SELECT * FROM MessagesDB WHERE `to`=:myName OR `from`=:myName")
    fun getAllInbox(myName: String = Messages.MY_NAME): List<MessagesEntity>

    @Query("SELECT * FROM MessagesDB WHERE `to`=:name")
    fun getAllFromChannel(name: String): List<MessagesEntity>
}