package com.example.data.chat.api

import com.example.data.chat.api.model.Chat
import com.example.data.user.api.model.UserData
import com.google.firebase.firestore.DocumentReference

interface ChatRepository {
    suspend fun getChats(user: UserData): List<Chat>
    suspend fun createChat(userIds: List<String>)
    suspend fun deleteChat(id: String)
}