package com.example.swifttext.service

import com.example.swifttext.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class AuthService(private val auth: FirebaseAuth, private val ref: CollectionReference) {
    suspend fun createUser(user: User) {
        val res = auth.createUserWithEmailAndPassword(user.email, user.password).await()
        res.user?.uid?.let {
            ref.document(it).set(user.copy(id = it))
        }

    }

    suspend fun login(email: String, pass: String): Boolean {
        val res = auth.signInWithEmailAndPassword(email, pass).await()
        return res.user?.uid != null
    }

    fun isAuthenticate(): Boolean {
        auth.currentUser ?: return false
        return true
    }

    fun deAuthenticate() {
        auth.signOut()
    }

    suspend fun getCurrentUser(): User? {
        return auth.uid?.let {
            ref.document(it).get().await().toObject(User::class.java)
        }
    }

    fun getUserUid(): String? {
        return auth.uid
    }

    suspend fun getAllUser(): Flow<MutableList<User>> {
        val users = mutableListOf<User>()
        val res = ref.orderBy("score", Query.Direction.DESCENDING).get().await()

        for (user in res) {
            users.add(user.toObject(User::class.java).copy(id = user.id))
        }

        return flowOf(users)
    }

}