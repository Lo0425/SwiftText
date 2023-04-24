package com.example.swifttext.di

import android.content.Context
import android.content.SharedPreferences
import com.example.swifttext.data.repositories.FireStoreRulesRepository
import com.example.swifttext.domain.repository.RulesRepository
import com.example.swifttext.data.repositories.UserRepositoryImpl
import com.example.swifttext.domain.repository.UserRepository
import com.example.swifttext.service.AuthService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyAppDependency {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences {
        if (sharedPref == null) {
            val key = context.packageName ?: throw NullPointerException("")
            sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        }
        return sharedPref!!
    }
    private var sharedPref: SharedPreferences? = null


    @Provides
    @Singleton
    fun getFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun getFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun getAuthRepository(auth: FirebaseAuth, db: FirebaseFirestore): AuthService {
        return AuthService(auth, db.collection("users"))
    }

    @Provides
    @Singleton
    fun provideUserRepository(db: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(db.collection("users"))
    }

    @Provides
    @Singleton
    fun provideRulesRepository(db: FirebaseFirestore): RulesRepository {
        return FireStoreRulesRepository(db.collection("rules"))
    }


}