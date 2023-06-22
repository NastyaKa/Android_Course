package com.gitlab.nastyaka.chat

import android.content.Context
import com.gitlab.nastyaka.chat.database.ChatsDAO
import com.gitlab.nastyaka.chat.database.ChatsDB
import com.gitlab.nastyaka.chat.database.MessagesDAO
import com.gitlab.nastyaka.chat.database.MessagesDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class Providers {

    @Singleton
    @Provides
    fun provideChatsDAO(@ApplicationContext context: Context): ChatsDAO {
        return ChatsDB.getDatabase(context).chatsDao()
    }

    @Singleton
    @Provides
    fun provideMessagesDAO(@ApplicationContext context: Context): MessagesDAO {
        return MessagesDB.getDatabase(context).msgsDao()
    }

    @Singleton
    @Provides
    fun provideMessengerService(): ChatApi {
        return ChatApi.getApi()
    }
}