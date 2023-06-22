package com.gitlab.nastyaka.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val name: String
) : Parcelable
