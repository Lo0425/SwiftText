package com.example.swifttext.ui.presentation.deactivatedRulesFragment.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.swifttext.data.model.Rules
import com.example.swifttext.domain.repository.RulesRepository
import com.example.swifttext.service.AuthService
import com.example.swifttext.ui.presentation.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeactivatedRulesViewModel @Inject constructor(
    private val repo: RulesRepository,
    private val authService: AuthService
): BaseViewModel() {
    val rules: MutableLiveData<List<Rules>> = MutableLiveData()
    val isLoading: MutableSharedFlow<Boolean> = MutableSharedFlow()

    override fun onViewCreated() {
        super.onViewCreated()
        viewModelScope.launch {
            val user = authService.getCurrentUser()
            val email = user?.email

            if (email != null) {
                getDeactivatedRulesByEmail(email)
            }

        }
    }

    private fun getDeactivatedRulesByEmail(email: String) {
        viewModelScope.launch {
            isLoading.emit(true)
            val res = safeApiCall { repo.getDeactivatedRulesByEmail(email) }
            res?.let {
                rules.value = it
            }
            isLoading.emit(false)

        }

    }
}