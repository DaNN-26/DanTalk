package com.example.network.auth.domain.repository

import com.example.network.auth.domain.model.User

interface UserRepository {
    suspend fun createUser(user: User)
    suspend fun isValueExists(field: String, value: String): Boolean
}