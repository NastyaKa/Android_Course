package com.gitlab.nastyaka.chat

import com.squareup.moshi.Json

data class Messages(
    val id: Int?,
    val from: String?,
    val to: String?,
    val time: Long?,
    val data: MessagesData?
) {
    companion object {
        const val MY_NAME = "qwerty123"
        const val FROM_ME_TXT = 0
        const val FROM_ME_PIC = 1
        const val FROM_OTHER_TXT = 2
        const val FROM_OTHER_PIC = 3
    }
}

data class MessagesData(
    @field:Json(name = "Text") val text: MessagesTxt?,
    @field:Json(name = "Image") val image: MessagesImg?
)

data class MessagesTxt(val text: String)

data class MessagesImg(@field:Json(name = "link") val imageLink: String?)
