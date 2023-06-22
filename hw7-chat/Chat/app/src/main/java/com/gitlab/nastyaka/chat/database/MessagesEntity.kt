package com.gitlab.nastyaka.chat.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MessagesDB")
data class MessagesEntity(
    @PrimaryKey val id: Int,
    val from: String?,
    val to: String?,
    val text: String?,
    val imageLink: String?,
    val time: Long?
)
