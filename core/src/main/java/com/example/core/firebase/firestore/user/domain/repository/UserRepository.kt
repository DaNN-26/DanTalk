package com.example.core.firebase.firestore.user.domain.repository

import com.example.domain.model.userdata.UserData
import com.google.firebase.firestore.DocumentReference

interface UserRepository {
    suspend fun createUser(userData: UserData)
    suspend fun getUser(userId: String): UserData
    suspend fun getUserReference(userId: String): DocumentReference
    suspend fun isValueExists(field: String, value: String): Boolean
    suspend fun getUsersByQuery(query: String): List<UserData>
    suspend fun updateUser(userData: UserData)
}