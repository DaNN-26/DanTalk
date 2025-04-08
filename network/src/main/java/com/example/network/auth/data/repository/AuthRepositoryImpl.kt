package com.example.network.auth.data.repository

import android.util.Log
import com.example.network.auth.domain.model.User
import com.example.network.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override suspend fun createUser(email: String, password: String): String =
        suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val user = firebaseAuth.currentUser
                    Log.d("FirebaseAuth", user?.email ?: "Error")
                    continuation.resume(user?.uid ?: "")
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }

    override suspend fun login(email: String, password: String): User =
        suspendCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val user = firebaseAuth.currentUser
                    Log.d("FirebaseAuth", user?.email ?: "Error")
                    continuation.resume(User(id = user!!.uid, email = user.email.toString()))
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
}