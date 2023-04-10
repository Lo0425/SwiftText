package com.example.swifttext.ui.presentation.login

import com.example.swifttext.data.model.User

sealed class LoginEvent{
    data class Login(val user: User): LoginEvent()
}