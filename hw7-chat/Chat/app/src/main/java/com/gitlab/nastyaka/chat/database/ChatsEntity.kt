package com.gitlab.nastyaka.chat.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatsEntity(
    @PrimaryKey @ColumnInfo(name = "id") val name: String
)