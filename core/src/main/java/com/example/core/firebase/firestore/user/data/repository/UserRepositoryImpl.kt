package com.example.core.firebase.firestore.user.data.repository

import android.util.Log
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.example.domain.userdata.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun createUser(userData: UserData) {
        val data = hashMapOf(
            "email" to userData.email,
            "username" to userData.username,
            "firstname" to userData.firstname,
            "lastname" to userData.lastname,
            "patronymic" to userData.patronymic
        )
        firestore.collection("users").document(userData.userId)
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
                    if(it != null) {
                        val response = it.toObject(UserData::class.java)!!
                        val userData = response.copy(userId = userId)
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
                    if(document != null && document.documents.isNotEmpty())
                        continuation.resume(true)
                    else
                        continuation.resume(false)
                }
                .addOnFailureListener { Log.d("UserRepository", "exception: ${it.message}") }
        }
}