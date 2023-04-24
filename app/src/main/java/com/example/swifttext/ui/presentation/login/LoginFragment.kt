package com.example.swifttext.ui.presentation.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.swifttext.R
import com.example.swifttext.databinding.FragmentLoginBinding
import com.example.swifttext.ui.presentation.base.BaseFragment
import com.example.swifttext.ui.presentation.login.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val viewModel: LoginViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_login

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()


        binding?.btnLogin?.setOnClickListener {
            Log.d("debugging", "Logging in")
            viewModel.login()
        }


        var counter = 0;

        binding?.run {
            btnShowPassword.setOnClickListener {
                if(counter == 0){
                    etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance();
                    btnShowPassword.setBackgroundResource(R.drawable.show_password)
                    counter = 1
                }else{
                    etPassword.transformationMethod = PasswordTransformationMethod.getInstance();
                    btnShowPassword.setBackgroundResource(R.drawable.hide_password)
                    counter = 0
                }
            }



            btnSignUp.setOnClickListener{
                val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
                navController.navigate(action)
            }


        }

    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.loginFinish.collect{
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                navController.navigate(action)
            }
        }

    }

//    private fun navigateToHome(){
//        (requireContext().applicationContext)
//    }



}