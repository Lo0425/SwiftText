package com.example.swifttext.ui.presentation.signup.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.swifttext.data.model.User
import com.example.swifttext.service.AuthService
import com.example.swifttext.ui.presentation.base.viewModel.BaseViewModel
import com.example.swifttext.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authRepo: AuthService) : BaseViewModel() {
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    val username: MutableStateFlow<String> = MutableStateFlow("")
    val email: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")
    val retypePassword: MutableStateFlow<String> = MutableStateFlow("")

    fun signUp() {
        if (Utils.validate(username.value, email.value, password.value,retypePassword.value)) {
            if(password.value == retypePassword.value) {

                viewModelScope.launch {
                    safeApiCall {
                        authRepo.createUser(
                            User(
                                username = username.value,
                                email = email.value,
                                password = password.value,
                                password2 = retypePassword.value
                            )
                        )
                    }
                    finish.emit(Unit)
                }
            }else{
                viewModelScope.launch {
                    error.emit("Password and retyped-password are not match")
                }
            }
        } else {
            viewModelScope.launch {
                error.emit("Please provide all the information")
            }
        }

    }


}