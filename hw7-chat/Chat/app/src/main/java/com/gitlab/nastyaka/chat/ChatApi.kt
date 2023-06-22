package com.gitlab.nastyaka.chat

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface ChatApi {

    @GET("channels")
    suspend fun getChannels(): List<String>

    @GET("inbox/{user}")
    suspend fun getInbox(
        @Path("user") userName: String,
        @Query("lastKnownId") lastKnownId: Int,
        @Query("limit") limit: Int
    ): List<Messages>

    @GET("channel/{name}")
    suspend fun getChannelMsgs(
        @Path("name") channelName: String,
        @Query("lastKnownId") lastKnownId: Int,
        @Query("limit") limit: Int
    ): List<Messages>

    @POST("messages")
    suspend fun postMessage(@Body message: Messages): Int

    @POST("messages")
    suspend fun postImage(@Body body: MultipartBody): Int

    companion object {

        @Volatile
        private var INSTANCE: ChatApi? = null

        fun getApi(): ChatApi {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildApi()
                }
            }
            return INSTANCE!!
        }

        private fun buildApi(): ChatApi {
            return Retrofit.Builder()
                .baseUrl("http://213.189.221.170:8008/")
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(ChatApi::class.java)
        }
    }
}