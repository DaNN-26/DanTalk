package com.example.core.firebase.firestore.user.domain.repository

import com.example.domain.userdata.model.UserData

interface UserRepository {
    suspend fun createUser(userData: UserData)
    suspend fun getUser(userId: String): UserData
    suspend fun isValueExists(field: String, value: String): Boolean
}