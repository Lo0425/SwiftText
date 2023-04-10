package com.example.swifttext.ui.presentation.base.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class BaseViewModel : ViewModel() {
    val error: MutableSharedFlow<String> = MutableSharedFlow()
    open fun onViewCreated() {

    }

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): T? {
        return try {
            apiCall.invoke()
        } catch (e: Exception) {
            error.emit(e.message.toString())
            null
        }
    }
}