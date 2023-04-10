package com.example.swifttext.domain.usecase

import com.example.swifttext.common.Resource
import com.example.swifttext.service.AuthService
import com.example.swifttext.ui.presentation.login.LoginEvent
import com.example.swifttext.utils.Utils
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authService: AuthService
) {
    operator fun invoke(event: LoginEvent) = flow<Resource<Boolean>> {

        try{
            when(event){
                is LoginEvent.Login -> {
                    val (_, _, pass,email) = event.user
                    if (Utils.validate()) {
                        emit(Resource.Loading(true))
                        val res = authService.login(email, pass)
                        emit(Resource.Success(res, "Login is successful"))
                    }else{
                        emit(Resource.Error("Login is Failed"))
                    }
                }
            }
        }catch(e: Exception){
            emit(Resource.Error("Something went wrong"))
        }
    }
}