package com.example.data.auth.api

interface AuthRepository {
    suspend fun createUser(email: String, password: String) : Result<String>
    suspend fun login(email: String, password: String) : Result<String>
    suspend fun signOut()
}