package com.example.swifttext.data.repositories

import android.util.Log
import com.example.swifttext.data.model.Rules
import com.example.swifttext.domain.repository.RulesRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FireStoreRulesRepository(private val ref: CollectionReference): RulesRepository {
    override suspend fun getRules(): List<Rules> {
        val rules = mutableListOf<Rules>()
        val res = ref.orderBy("date", Query.Direction.DESCENDING).get().await()
        for(document in res){
            rules.add(document.toObject(Rules::class.java).copy(id = document.id))
        }
        return rules
    }

    override suspend fun getRulesByEmail(email: String): List<Rules> {
        val rules = mutableListOf<Rules>()
        val res = ref.orderBy("date", Query.Direction.DESCENDING)
            .whereEqualTo("userEmail", email)
            .get().await()
        for(document in res){
            rules.add(document.toObject(Rules::class.java).copy(id = document.id))
        }
        return rules
    }

    override suspend fun getActivatedRulesByEmail(email: String): List<Rules> {
        val rules = mutableListOf<Rules>()

        val res = ref.orderBy("date", Query.Direction.DESCENDING)
            .whereEqualTo("userEmail", email)
            .whereEqualTo("status",true)
            .get().await()
        for(document in res){
            rules.add(document.toObject(Rules::class.java).copy(id = document.id))
        }
        return rules

    }


    override suspend fun getDeactivatedRulesByEmail(email: String): List<Rules> {
        val rules = mutableListOf<Rules>()

        val res = ref.orderBy("date", Query.Direction.DESCENDING)
            .whereEqualTo("userEmail", email)
            .whereEqualTo("status",false)
            .get().await()
        for(document in res){
            rules.add(document.toObject(Rules::class.java).copy(id = document.id))
        }
        return rules

    }

    override suspend fun addRules(rules: Rules) {
        ref.add(rules).await()
    }

    override suspend fun getRuleById(id: String): Rules?{
        val res = ref.document(id).get().await()
        return res.toObject(Rules::class.java)?.copy(id=id)
    }

    override suspend fun updateRules(id: String, rules: Rules): Rules? {
        val update = rules.copy(id = id)
        ref.document(id).set(rules).await()
        return update
    }

    override suspend fun deleteRules(id: String) {
        ref.document(id).delete().await()
    }


}