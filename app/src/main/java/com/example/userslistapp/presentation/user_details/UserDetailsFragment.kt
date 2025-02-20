package com.example.userslistapp.presentation.user_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.userslistapp.R
import com.example.userslistapp.databinding.FragmentUserDetailsBinding
import com.example.userslistapp.presentation.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailsFragment : Fragment(R.layout.fragment_user_details) {

    private val binding by viewBinding(FragmentUserDetailsBinding::bind)
    private val viewModel by viewModels<UserDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is UserDetailsState.Loading -> showLoading()
                    is UserDetailsState.Success -> showUser(state.user)
                    is UserDetailsState.Error -> showError(state.message)
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.GONE
    }

    private fun showUser(user: User) {
        binding.progressBar.visibility = View.GONE
        binding.errorTextView.visibility = View.GONE

        with(binding) {
            tvName.text = user.name
            tvEmail.text = user.email
            tvCity.text = user.city
            tvPhone.text = user.phone
        }
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.errorTextView.visibility = View.VISIBLE
        binding.errorTextView.text = message
    }
}