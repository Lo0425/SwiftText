package com.example.swifttext.ui.presentation.signup

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.swifttext.R
import com.example.swifttext.databinding.FragmentSignUpBinding
import com.example.swifttext.ui.presentation.base.BaseFragment
import com.example.swifttext.ui.presentation.login.viewModel.LoginViewModel
import com.example.swifttext.ui.presentation.signup.viewModel.SignUpViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {
    override val viewModel: SignUpViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_sign_up
    var counter = 0;
    var counter2 = 0;

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner  = viewLifecycleOwner

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()

        binding?.btnRegister?.setOnClickListener {
            val username = binding?.etUsername?.text.toString()
            val password = binding?.etPassword?.text.toString()

            if (username.length < 2 && password.length < 6) {
                val snackBar =
                    Snackbar.make(
                        binding!!.root,
                        "Please enter all the values",
                        Snackbar.LENGTH_LONG
                    )
                snackBar.show()
            } else {
                lifecycleScope.launch {
                    Log.d("debugging", "viewModel.signUp")
                    viewModel.signUp()
                }
            }
        }

        binding?.run {
            btnShowPassword.setOnClickListener {
                if (counter == 0) {
                    etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance();
                    btnShowPassword.setBackgroundResource(R.drawable.show_password)
                    counter = 1
                } else {
                    etPassword.transformationMethod = PasswordTransformationMethod.getInstance();
                    btnShowPassword.setBackgroundResource(R.drawable.hide_password)
                    counter = 0
                }
            }

            btnShowPassword2.setOnClickListener {
                if (counter2 == 0) {
                    etPassword2.transformationMethod = HideReturnsTransformationMethod.getInstance();
                    btnShowPassword2.setBackgroundResource(R.drawable.show_password)
                    counter2 = 1
                } else {
                    etPassword2.transformationMethod = PasswordTransformationMethod.getInstance();
                    btnShowPassword2.setBackgroundResource(R.drawable.hide_password)
                    counter2 = 0
                }
            }


            btnLogin.setOnClickListener {
                val action = SignUpFragmentDirections.actionGlobalLoginFragment()
                navController.navigate(action)
            }



        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.finish.collect {
                navController.navigate(R.id.loginFragment)
            }
        }
    }

}