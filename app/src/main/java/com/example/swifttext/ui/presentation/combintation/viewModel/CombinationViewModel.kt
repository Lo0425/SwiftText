package com.example.swifttext.ui.presentation.combintation.viewModel

import android.os.Build
import android.text.Editable
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.swifttext.data.model.Rules
import com.example.swifttext.domain.repository.RulesRepository
import com.example.swifttext.service.AuthService
import com.example.swifttext.ui.presentation.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CombinationViewModel @Inject constructor(
    private val authService: AuthService,
    private val repo: RulesRepository
) : BaseViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun addRules(textIncludes: Editable, reply: Editable, status: Boolean) {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")
            val current = LocalDateTime.now().format(formatter)

            val rule = Rules(
                authService.getUserUid().toString(),
                authService.getCurrentUser()?.email.toString(),
                authService.getCurrentUser()?.username.toString(),
                textIncludes.toString(),
                reply.toString(),
                status,
                current.toString()
            )

            repo.addRules(rule)

        }
    }
}