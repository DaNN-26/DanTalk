package com.example.core.datastore.domain

import com.example.domain.userdata.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataStore {
    val getUserData: Flow<UserData>
    suspend fun saveUserData(userData: UserData)
    suspend fun clearUserData()
}