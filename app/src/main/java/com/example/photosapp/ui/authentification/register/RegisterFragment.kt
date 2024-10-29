package com.example.photosapp.ui.authentification.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.photosapp.common.base.BaseFragment
import com.example.photosapp.databinding.FragmentRegisterBinding
import com.example.photosapp.domain.model.auth.SignUp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.registerState.collect { registerState ->
                updateUI(registerState)
            }
        }

        with(binding) {
            signupButton.setOnClickListener {
                val signUpCredentials =
                    SignUp(
                        login = loginTextInputField.text.toString(),
                        password = passwordTextInputField.text.toString(),
                        repeatPassword = repeatPasswordTextInputField.text.toString()
                    )
                viewModel.signUp(signUpCredentials)
            }
        }

    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegisterBinding = FragmentRegisterBinding.inflate(inflater, container, false)

    private fun updateUI(state: RegisterState) {

        binding.apply {

            loadingIndicator.isVisible = state.isLoading
            errorTextView.isVisible = state.errorText != null
            state.errorText?.let { errorTextView.text = it }

            if (state.success) {
                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                loginTextInputField.text?.clear()
                passwordTextInputField.text?.clear()
                repeatPasswordTextInputField.text?.clear()
            }
        }
    }

}