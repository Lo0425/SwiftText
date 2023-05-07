package com.example.swifttext.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.example.swifttext.data.model.User
import com.example.swifttext.service.AuthService
import com.example.swifttext.ui.presentation.signup.viewModel.SignUpViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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
class RegisterViewModelTest {
    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SignUpViewModel
    private val authRepo = Mockito.mock(AuthService::class.java)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = SignUpViewModel(authRepo)
    }

    @Test
    fun `user should not be able to attempt registering without providing valid information`() =
        runTest {
            viewModel.username.value = ""
            viewModel.email.value = ""
            viewModel.password.value = ""
            viewModel.retypePassword.value = ""
            assertEquals(viewModel.isFormValid(), false)
        }

    @Test
    fun `user should be able to attempt at registering a new account after providing valid information`() =
        runTest {
            viewModel.username.value = "alphabetman"
            viewModel.email.value = "abc@abc.com"
            viewModel.password.value = "qweqweqwe"
            viewModel.retypePassword.value = "qweqweqwe"
            assertEquals(viewModel.isFormValid(), true)
        }

    @Test
    fun `user should be able to register a new account after passing validation`() = runTest {
        Mockito.`when`(authRepo.createUser(User("alphabetman", "abc@abc.com", "qweqweqwe")))
            .thenReturn(Unit)
        viewModel.username.value = "alphabetman"
        viewModel.email.value = "abc@abc.com"
        viewModel.password.value = "qweqweqwe"
        viewModel.retypePassword.value = "qweqweqwe"
        viewModel.signUp()
        assertEquals(viewModel.finish.first(), Unit)
    }

    @Test
    fun `user should not be able to register a new account without first passing validation`() =
        runTest {
            Mockito.`when`(authRepo.createUser(User("alphabetman", "abc@abc.com", "qweqweqwe")))
                .thenReturn(Unit)
            viewModel.username.value = "alp"
            viewModel.email.value = ".abc@abc.com"
            viewModel.password.value = "qweqweqwe"
            viewModel.retypePassword.value = "qweqwe"
            viewModel.signUp()
            assertEquals(viewModel.error.first(), "Please provide the proper information")
        }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }
}