package com.gitlab.nastyaka.chat.repos

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gitlab.nastyaka.chat.*
import com.gitlab.nastyaka.chat.database.ChatsDAO
import com.gitlab.nastyaka.chat.database.ChatsEntity
import com.gitlab.nastyaka.chat.database.MessagesDAO
import com.gitlab.nastyaka.chat.database.MessagesEntity
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import okhttp3.Headers.Companion.headersOf
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.net.ConnectException
import java.net.URLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val msgsDAO: MessagesDAO,
    private val chatsDAO: ChatsDAO,
    private val chatApi: ChatApi
) {

    private val chats = MutableLiveData<List<Chat>>(mutableListOf())
    private val inbox = MutableLiveData<List<Messages>>(mutableListOf())
    private val channelMsgs = MutableLiveData<List<Messages>>(mutableListOf())
    private val inboxMsgs = MutableLiveData<List<Messages>>(mutableListOf())
    private var channelName: String? = null
    private val mainLooper = Handler(Looper.getMainLooper())
    private var lastIdInbox = 0
    private var lastIdMsgs = 0
    private val msgsAdapter = Moshi.Builder().build().adapter(Messages::class.java)

    private val inboxUpdater = object : Runnable {
        override fun run() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var newChats: Set<Chat> = mutableSetOf()
                    var newInbox = chatApi.getInbox(Messages.MY_NAME, lastIdInbox, 10)
                    if (newInbox.isNotEmpty()) {
                        withContext(Dispatchers.Main) {
                            lastIdInbox = newInbox.first().id!!
                        }
                        newInbox =
                            newInbox.filter { msg ->
                                !msg.from!!.endsWith("@channel") && !msg.to!!.endsWith(
                                    "@channel"
                                )
                            }
                        for (msg in newInbox) {
                            msgsDAO.insert(
                                MessagesEntity(
                                    msg.id!!,
                                    msg.from,
                                    msg.to,
                                    msg.data?.text?.text,
                                    msg.data?.image?.imageLink,
                                    msg.time
                                )
                            )
                        }
                        withContext(Dispatchers.Main) {
                            inbox.value = inbox.value?.plus(newInbox)?.distinctBy { msg -> msg.id }
                                ?.sortedBy { msg -> msg.id }?.reversed()
                        }
                        newChats = newChats.plus(newInbox.map {
                            if (it.from == Messages.MY_NAME) {
                                Chat(it.to!!)
                            } else {
                                Chat(it.from!!)
                            }
                        }.toSet().minus(chats.value!!.toSet()))
                        for (chat in newChats) {
                            chatsDAO.insert(ChatsEntity(chat.name))
                        }
                        if (newChats.isNotEmpty()) {
                            withContext(Dispatchers.Main) {
                                chats.value =
                                    chats.value?.plus(newChats)?.distinctBy { chat -> chat.name }
                            }
                        }
                        val newInboxMsg =
                            newInbox.filter { msg -> msg.from == channelName || msg.to == channelName }
                        if (newInboxMsg.isNotEmpty()) {
                            withContext(Dispatchers.Main) {
                                inboxMsgs.value =
                                    inboxMsgs.value?.plus(newInbox)?.distinctBy { msg -> msg.id }
                                        ?.sortedBy { msg -> msg.id }?.reversed()
                            }
                        }
                        run()
                    }
                    delay(2300)
                    run()
                } catch (e: ConnectException) {
                    Log.e(e::class.java.simpleName, e.message.toString())
                }
            }
        }
    }

    private val channelsUpdater = object : Runnable {
        override fun run() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val newChats =
                        chatApi.getChannels().map { name -> Chat(name) }.toSet()
                            .minus(chats.value!!.toSet())
                    if (newChats.isNotEmpty()) {
                        withContext(Dispatchers.Main) {
                            chats.value =
                                chats.value?.plus(newChats)?.distinctBy { chat -> chat.name }
                        }
                        for (chat in newChats) {
                            chatsDAO.insert(ChatsEntity(chat.name))
                        }
                    }
                    delay(2300)
                    run()
                } catch (e: ConnectException) {
                    Log.e(e::class.java.simpleName, e.message.toString())
                }
            }
        }
    }

    private val msgsUpdater = object : Runnable {
        override fun run() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val newMsgs = chatApi.getChannelMsgs(channelName!!, lastIdMsgs, 10)
                    for (msg in newMsgs) {
                        msgsDAO.insert(
                            MessagesEntity(
                                msg.id!!,
                                msg.from,
                                msg.to,
                                msg.data?.text?.text,
                                msg.data?.image?.imageLink,
                                msg.time
                            )
                        )
                    }
                    if (newMsgs.isNotEmpty()) {
                        withContext(Dispatchers.Main) {
                            lastIdMsgs = newMsgs.first().id!!
                            channelMsgs.value =
                                channelMsgs.value?.plus(newMsgs)?.distinctBy { msg -> msg.id }
                                    ?.sortedBy { msg -> msg.id }?.reversed()
                        }
                        run()
                    }
                    delay(2300)
                    run()
                } catch (e: ConnectException) {
                    Log.e(e::class.java.simpleName, e.message.toString())
                }
            }
        }
    }

    fun getChatList(): LiveData<List<Chat>> {
        CoroutineScope(Dispatchers.IO).launch {
            val oldChats: List<Chat>
            var oldInbox: List<Messages>
            try {
                oldChats = chatsDAO.getAll().map { chat -> Chat(chat.name) }
                oldInbox = msgsDAO.getAllInbox().map { msg ->
                    Messages(
                        msg.id, msg.from, msg.to, msg.time, MessagesData(
                            if (msg.text == null) {
                                null
                            } else {
                                MessagesTxt(msg.text)
                            },
                            if (msg.imageLink == null) {
                                null
                            } else {
                                MessagesImg(msg.imageLink)
                            }
                        )
                    )
                }
                withContext(Dispatchers.Main) {
                    lastIdInbox = inbox.value?.maxOfOrNull { msg -> msg.id!! } ?: 0
                }
                oldInbox =
                    oldInbox.filter { msg ->
                        !msg.from!!.endsWith("@channel") && !msg.to!!.endsWith(
                            "@channel"
                        )
                    }
                        .sortedBy { msg -> msg.id }.reversed()
                withContext(Dispatchers.Main) {
                    chats.value = (oldChats.toList() + oldInbox.map { msg ->
                        if (msg.from == Messages.MY_NAME) {
                            Chat(msg.to!!)
                        } else {
                            Chat(msg.from!!)
                        }
                    }).distinctBy { chat -> chat.name }
                    inbox.value = oldInbox
                }
                inboxUpdater.run()
                channelsUpdater.run()
            } catch (e: ConnectException) {
                Log.e(e::class.java.simpleName, e.message.toString())
            }
        }

        return chats
    }

    fun getMsgs(name: String): LiveData<List<Messages>> {
        mainLooper.removeCallbacks(msgsUpdater)
        inboxMsgs.value = listOf()
        channelMsgs.value = listOf()
        channelName = name
        CoroutineScope(Dispatchers.IO).launch {
            if (name.endsWith("@channel")) {
                val oldMsgs = msgsDAO.getAllFromChannel(name)
                    .map { msg ->
                        Messages(
                            msg.id, msg.from, msg.to, msg.time, MessagesData(
                                if (msg.text == null) {
                                    null
                                } else {
                                    MessagesTxt(msg.text)
                                },
                                if (msg.imageLink == null) {
                                    null
                                } else {
                                    MessagesImg(msg.imageLink)
                                }
                            )
                        )
                    }.sortedBy { msg -> msg.id }.reversed()
                withContext(Dispatchers.Main) {
                    channelMsgs.value = oldMsgs
                    lastIdMsgs = oldMsgs.maxOfOrNull { msg -> msg.id!! } ?: 0
                    msgsUpdater.run()
                }
            } else {
                withContext(Dispatchers.Main) {
                    inboxMsgs.value =
                        inbox.value?.filter { msg -> msg.from == name || msg.to == name }
                            ?.distinctBy { msg -> msg.id }?.sortedBy { msg -> msg.id }?.reversed()
                }
            }
        }
        return if (name.endsWith("@channel")) {
            channelMsgs
        } else {
            inboxMsgs
        }
    }

    fun sendText(text: String, name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                chatApi.postMessage(
                    Messages(
                        null,
                        Messages.MY_NAME,
                        name,
                        null,
                        MessagesData(MessagesTxt(text), null)
                    )
                )
            } catch (e: ConnectException) {
                Log.e(e::class.java.simpleName, e.message.toString())
            }
        }
    }

    fun sendPhoto(path: String, name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val imageName = path + System.currentTimeMillis().toString()
            val imageFile = File(path)

            try {
                chatApi.postImage(
                    MultipartBody.Builder().setType("multipart/form-data".toMediaType())
                        .addFormDataPart(
                            "msg",
                            msgsAdapter.toJson(Messages(null, Messages.MY_NAME, name, null, null))
                                ?: return@launch
                        ).addPart(
                        headersOf("filename", imageName), imageFile.asRequestBody(
                            URLConnection.guessContentTypeFromName(
                                path
                            ).toMediaType()
                        )
                    ).build()
                )
            } catch (e: ConnectException) {
                Log.e(e::class.java.simpleName, e.message.toString())
            } catch (e: IOException) {
                Log.e(e::class.java.simpleName, e.message.toString())
            }
        }
    }
}