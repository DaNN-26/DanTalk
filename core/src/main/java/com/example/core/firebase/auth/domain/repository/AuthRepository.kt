package com.example.core.firebase.auth.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun createUser(email: String, password: String)
    suspend fun login(email: String, password: String) : Result<FirebaseUser>
    fun getCurrentUser(): FirebaseUser
}