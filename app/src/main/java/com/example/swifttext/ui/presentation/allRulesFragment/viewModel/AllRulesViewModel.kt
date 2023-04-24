package com.example.swifttext.ui.presentation.allRulesFragment.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
class AllRulesViewModel @Inject constructor(
    private val repo: RulesRepository,
    private val authService: AuthService
) : BaseViewModel() {



    var rules: MutableLiveData<List<Rules>> = MutableLiveData()
    val isLoading: MutableSharedFlow<Boolean> = MutableSharedFlow()

    override fun onViewCreated() {
        super.onViewCreated()
        viewModelScope.launch {
            val user = authService.getCurrentUser()
            val email = user?.email

            if (email != null) {
                getRulesByEmail(email)
            }

        }
    }

//    class Provider(val repo: RulesRepository): ViewModelProvider.Factory{
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return AllRulesViewModel(repo) as T
//        }
//    }


    private fun getRulesByEmail(email: String) {
        viewModelScope.launch {
            isLoading.emit(true)
            val res = safeApiCall { repo.getRulesByEmail(email) }
            res?.let {
                rules.value = it
            }
            isLoading.emit(false)
        }

    }
}