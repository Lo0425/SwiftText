package com.example.swifttext.data.repositories

import com.example.swifttext.data.model.User
import com.example.swifttext.domain.repository.UserRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl (private val ref: CollectionReference): UserRepository {
    override suspend fun getUsers(): List<User> {
        val res = ref.get().await()
        val users = mutableListOf<User>()
        res.documents.forEach {
            it.toObject(User::class.java)?.let { user ->
                users.add(user)
            }
        }
        return users
    }
}
