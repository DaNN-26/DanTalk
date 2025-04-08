package com.example.network.auth.data.repository

import android.util.Log
import com.example.network.auth.domain.model.User
import com.example.network.auth.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun createUser(user: User) {
        val userData = hashMapOf(
            "id" to user.id,
            "email" to user.email,
            "username" to user.username,
            "firstname" to user.firstname,
            "lastname" to user.lastname,
            "patronymic" to user.patronymic
        )
        firestore.collection("users").document()
            .set(userData)
            .addOnSuccessListener { Log.d("UserRepository", "createUser: $user") }
            .addOnFailureListener { Log.d("UserRepository", "exception: ${it.message}") }
    }

    override suspend fun isValueExists(field: String, value: String): Boolean =
        suspendCoroutine { continuation ->
            firestore.collection("users")
                .whereEqualTo(field, value)
                .get()
                .addOnSuccessListener { document ->
                    if(document != null && document.documents.isNotEmpty())
                        continuation.resume(true)
                    else
                        continuation.resume(false)
                }
                .addOnFailureListener { Log.d("UserRepository", "exception: ${it.message}") }
        }
}