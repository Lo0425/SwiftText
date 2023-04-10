package com.example.swifttext.domain.repository

import com.example.swifttext.data.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}