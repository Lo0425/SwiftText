package com.example.swifttext.ui.presentation.editCombination.viewModel

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.swifttext.data.model.Rules
import com.example.swifttext.domain.repository.RulesRepository
import com.example.swifttext.service.AuthService
import com.example.swifttext.ui.presentation.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class EditCombinationViewModel @Inject constructor(
    private val authService: AuthService,
    private val repo: RulesRepository
) : BaseViewModel() {
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    val rules: MutableLiveData<Rules> = MutableLiveData()

    fun getProductById(id: String) {
        viewModelScope.launch {
            val res = repo.getRuleById(id)
            res.let {
                rules.value = it
            }
        }
    }

    fun deleteRule(id:String){
        viewModelScope.launch {
            try {
                safeApiCall { repo.deleteRules(id) }
                finish.emit(Unit)
            }catch(err:Exception){
                error.emit(err.message.toString())
            }
        }
    }

    fun editRules(textIncludes: Editable, reply: Editable, status: Boolean) {
        viewModelScope.launch {
            val rule = rules.value?.let {
                Rules(
                    it.id,
                    it.userEmail,
                    it.username,
                    textIncludes.toString(),
                    reply.toString(),
                    status,
                    it.date
                )
            }

            if (rule != null) {
                repo.updateRules(rule.id,rule)
            }
        }
    }
}