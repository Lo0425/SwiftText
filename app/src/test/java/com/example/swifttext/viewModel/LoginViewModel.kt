package com.example.swifttext.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.swifttext.domain.usecase.LoginUseCase
import com.example.swifttext.service.AuthService
import com.example.swifttext.ui.presentation.login.viewModel.LoginViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var useCase: LoginUseCase

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var loginViewModel: LoginViewModel
    private val authRepo = Mockito.mock(AuthService::class.java)

    @Before
    fun setup(){
        Dispatchers.setMain(StandardTestDispatcher())
        useCase = LoginUseCase(authRepo)
        loginViewModel = LoginViewModel(useCase)
    }


    @Test
    fun `user should be able to login with the correct credentials`() = runTest {
        Mockito.`when`(authRepo.login("abc@abc.com", "qweqweqwe")).thenReturn(true)
        loginViewModel.email.value = "abc@abc.com"
        loginViewModel.password.value = "qweqweqwe"
        loginViewModel.login()
        assertEquals(loginViewModel.loginFinish.first(), "Login success")
    }

    @Test
    fun `user should not be able to login with the wrong credentials`() = runTest {
        Mockito.`when`(authRepo.login("abcabc.com", "qweqweqwe")).thenReturn(false)
        loginViewModel.email.value = "abcabc.com"
        loginViewModel.password.value = "qweqweqwe"
        loginViewModel.login()

        assertEquals(loginViewModel.error.first(), "Login failed")
    }

    @After
    fun cleanup(){
        Dispatchers.resetMain()
    }
}