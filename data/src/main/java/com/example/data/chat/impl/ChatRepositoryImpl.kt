package com.example.data.chat.impl

import android.util.Log
import com.example.data.chat.api.ChatRepository
import com.example.data.chat.api.model.Chat
import com.example.data.chat.api.model.Message
import com.example.data.user.api.model.UserData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class ChatRepositoryImpl(
    private val firestore: FirebaseFirestore,
) : ChatRepository {
    @Suppress("UNCHECKED_CAST")
    override suspend fun getChats(user: UserData): List<Chat> {
        val userRef = getUserReference(user.userId)
        return suspendCoroutine { continuation ->
            firestore.collection("chats")
                .whereArrayContains("users", userRef)
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty || result == null) {
                        continuation.resume(emptyList())
                        return@addOnSuccessListener
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        val chats = result.documents.mapNotNull { document ->
                            try {
                                val usersRefs = document["users"] as? List<DocumentReference>
                                    ?: return@mapNotNull null
                                val users = usersRefs.map { ref ->
                                    ref.get().await().toObject(UserData::class.java)!!
                                        .copy(userId = ref.id)
                                }
                                Chat(
                                    id = document.id,
                                    users = users,
                                    messages = document["messages"] as? List<Message> ?: emptyList()
                                )
                            } catch (e: Exception) {
                                Log.d("Firestore", "Failed to get chat $e")
                                null
                            }
                        }
                        continuation.resume(chats)
                    }
                }
                .addOnFailureListener {
                    continuation.resume(emptyList())
                    Log.d("Firestore", "Failed to get chats $it")
                }
        }
    }

    override suspend fun createChat(userIds: List<String>) {
        val chatId = userIds.joinToString("")
        val users = listOf(
            firestore.collection("users").document(userIds[0]),
            firestore.collection("users").document(userIds[1])
        )

        val data = mapOf(
            "users" to users,
            "messages" to emptyList<Message>()
        )
        firestore.collection("chats").document(chatId)
            .set(data)
            .addOnSuccessListener { Log.d("Firestore", "Chat successfully created") }
            .addOnFailureListener { Log.d("Firestore", "Chat failed to create $it") }
    }

    override suspend fun deleteChat(id: String) {
        firestore.collection("chats").document(id)
            .delete()
            .addOnSuccessListener { Log.d("Firestore", "Chat successfully deleted") }
            .addOnFailureListener { Log.d("Firestore", "Chat failed to delete $it") }
    }

    private suspend fun getUserReference(userId: String): DocumentReference =
        suspendCoroutine { continuation ->
            firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { continuation.resume(it.reference) }
        }
}