package com.example.swifttext.domain.repository

import com.example.swifttext.data.model.Rules

interface RulesRepository {
    suspend fun getRules(): List<Rules>
    suspend fun getRulesByEmail(email: String): List<Rules>
    suspend fun getActivatedRulesByEmail(email: String): List<Rules>
    suspend fun getDeactivatedRulesByEmail(email: String): List<Rules>
    suspend fun addRules(rules: Rules)
    suspend fun getRuleById(id: String): Rules?
    suspend fun updateRules(id: String, rules: Rules): Rules?
    suspend fun deleteRules(id: String)
}