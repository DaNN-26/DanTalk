package com.example.core.firebase.firestore.chat.domain.repository

import com.example.domain.model.chat.Chat
import com.google.firebase.firestore.DocumentReference

interface ChatRepository {
    suspend fun getChats(userRef: DocumentReference): List<Chat>
    suspend fun createChat(userIds: List<String>)
    suspend fun deleteChat(id: String)
}