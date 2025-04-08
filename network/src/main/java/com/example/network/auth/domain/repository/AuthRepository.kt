package com.example.network.auth.domain.repository

import com.example.network.auth.domain.model.User

interface AuthRepository {
    suspend fun createUser(email: String, password: String): String
    suspend fun login(email: String, password: String): User
}