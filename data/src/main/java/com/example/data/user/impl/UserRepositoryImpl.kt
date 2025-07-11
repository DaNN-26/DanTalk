package com.example.data.user.impl

import android.util.Log
import com.example.data.user.api.UserRepository
import com.example.data.user.api.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class UserRepositoryImpl(
    private val firestore: FirebaseFirestore,
) : UserRepository {
    override suspend fun createUser(userData: UserData) {
        val data = hashMapOf(
            "email" to userData.email,
            "username" to userData.username,
            "firstname" to userData.firstname,
            "lastname" to userData.lastname,
            "patronymic" to userData.patronymic
        )
        firestore.collection("users").document(userData.id)
            .set(data)
            .addOnSuccessListener { Log.d("UserRepository", "createUser: $data") }
            .addOnFailureListener { Log.d("UserRepository", "exception: ${it.message}") }
    }

    override suspend fun getUser(userId: String): UserData {
        return suspendCoroutine { continuation ->
            firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener {
                    if (it != null) {
                        val response = it.toObject(UserData::class.java)!!
                        val userData = response.copy(id = userId)
                        continuation.resume(userData)
                    }
                }
                .addOnFailureListener { Log.d("UserRepository", "exception: ${it.message}") }
        }
    }

    override suspend fun isValueExists(field: String, value: String): Boolean =
        suspendCoroutine { continuation ->
            firestore.collection("users")
                .whereEqualTo(field, value)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.documents.isNotEmpty())
                        continuation.resume(true)
                    else
                        continuation.resume(false)
                }
                .addOnFailureListener { Log.d("UserRepository", "exception: ${it.message}") }
        }

    override suspend fun getUsersByQuery(query: String): List<UserData> =
        suspendCoroutine { continuation ->
            if (query.isBlank()) {
                continuation.resume(emptyList())
                return@suspendCoroutine
            }
            firestore.collection("users")
                .orderBy("username")
                .startAt(query)
                .endAt("$query\uf8ff")
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.documents.isNotEmpty()) {
                        val users = document.documents.map {
                            it.toObject(UserData::class.java)!!.copy(id = it.id)
                        }
                        continuation.resume(users)
                    } else
                        continuation.resume(emptyList())
                }
                .addOnFailureListener {
                    continuation.resume(emptyList())
                    Log.d("UserRepository", "exception: ${it.message}")
                }
        }

    override suspend fun updateUser(userData: UserData) {
        val data = mapOf(
            "email" to userData.email,
            "username" to userData.username,
            "firstname" to userData.firstname,
            "lastname" to userData.lastname,
            "patronymic" to userData.patronymic
        )
        firestore.collection("users").document(userData.id)
            .update(data)
            .addOnSuccessListener { Log.d("UserRepository", "updateUser: $data") }
            .addOnFailureListener { Log.d("UserRepository", "exception: ${it.message}") }
    }
}