package com.example.core.firebase.firestore.chat.domain.repository

import com.example.domain.chat.model.Chat
import com.example.domain.userdata.model.UserData
import com.google.firebase.firestore.DocumentReference

interface ChatRepository {
    suspend fun getChats(userRef: DocumentReference): List<Chat>
    suspend fun createChat(userIds: List<String>)
    suspend fun deleteChat(id: String)
}