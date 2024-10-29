package com.example.photosapp.ui.authentification.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.photosapp.R
import com.example.photosapp.common.base.BaseFragment
import com.example.photosapp.databinding.FragmentLoginBinding
import com.example.photosapp.domain.model.auth.SignIn
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { loginState ->
                updateUI(loginState)
            }
        }

        with(binding) {
            loginButton.setOnClickListener {
                val signUpCredentials =
                    SignIn(
                        login = loginTextInputField.text.toString(),
                        password = passwordTextInputField.text.toString()
                    )
                viewModel.signIn(signUpCredentials)
            }
        }
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    private fun updateUI(state: LoginState) {

        binding.apply {
            loadingIndicator.isVisible = state.isLoading
            errorTextView.isVisible = state.errorText != null
            state.errorText?.let { errorTextView.text = it }

            if (state.success) {
                navigateToPhotos(state)
                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()

                loginTextInputField.text?.clear()
                passwordTextInputField.text?.clear()
            }
        }
    }

    private fun navigateToPhotos(state: LoginState) {
        val action = LoginFragmentDirections.actionLoginFragmentToNavPhotos(
            state.signInResponse?.data?.login ?: "Unknown"
        )
        findNavController().navigate(
            action,
            NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build()
        )
        viewModel.resetState()
    }
}