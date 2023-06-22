package com.gitlab.nastyaka.chat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gitlab.nastyaka.chat.Chat
import com.gitlab.nastyaka.chat.repos.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject internal constructor(
    mainRepository: MainRepository
) : ViewModel() {
    val chatList: LiveData<List<Chat>> = mainRepository.getChatList()
}