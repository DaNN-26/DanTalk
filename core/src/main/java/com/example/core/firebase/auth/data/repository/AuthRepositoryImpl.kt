package com.example.core.firebase.auth.data.repository

import android.util.Log
import com.example.core.firebase.auth.domain.repository.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {
    override suspend fun createUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = firebaseAuth.currentUser
                Log.d("FirebaseAuth", user?.email ?: "Error")
            }
            .addOnFailureListener { /*TODO*/ }
    }

    override suspend fun login(email: String, password: String): Result<FirebaseUser> =
        suspendCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { task ->
                    val user = task.user
                    if (user != null) continuation.resume(success(user))
                }
                .addOnFailureListener { continuation.resume(failure(it)) }
        }

    override fun getCurrentUser(): FirebaseUser {
        Log.d("FirebaseAuth", firebaseAuth.currentUser?.email ?: "Error")
        return firebaseAuth.currentUser ?: throw FirebaseException("User not found")
    }
}