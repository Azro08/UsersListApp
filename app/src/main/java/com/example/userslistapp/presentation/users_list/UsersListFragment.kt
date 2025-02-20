package com.example.userslistapp.presentation.users_list

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.userslistapp.R
import com.example.userslistapp.databinding.FragmentUsersListBinding
import com.example.userslistapp.presentation.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersListFragment : Fragment(R.layout.fragment_users_list) {
    private val binding by viewBinding(FragmentUsersListBinding::bind)
    private val viewModel by viewModels<UsersListViewModel>()
    private var adapter: UsersListAdapter? = null
    private var scrollPosition = 0 //save so when nav back don't lose position
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupSwipeRefresh()
        observeUsersResponse()
        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt("SCROLL_POSITION")
            saveScrollPosition()
        }
    }

    private fun saveScrollPosition() {
        scrollPosition = (binding.rvUsers.layoutManager as LinearLayoutManager)
            .findFirstVisibleItemPosition()
    }

    private fun setupRecyclerView() {
        adapter = UsersListAdapter { navToDetails(it) }
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter
        binding.rvUsers.itemAnimator = DefaultItemAnimator()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.refreshData()
            observeUsersResponse()
        }
    }

    private fun observeUsersResponse() {
        lifecycleScope.launch {
            viewModel.responseStates.collectLatest { state ->
                when (state) {
                    is UsersUiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is UsersUiState.Success -> showUsers(state.data)
                    is UsersUiState.Error -> handleError(state.message)
                }
            }
        }
    }

    private fun showUsers(users: List<User>) {
        adapter?.submitData(users)
        binding.rvUsers.scrollToPosition(scrollPosition)
        binding.rvUsers.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.emptyTextView.visibility = View.GONE
        binding.errorTextView.visibility = View.GONE
    }

    private fun handleError(message: String) {
        binding.rvUsers.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.emptyTextView.visibility = View.GONE
        binding.errorTextView.visibility = View.VISIBLE
        binding.errorTextView.text = message
    }

    private fun navToDetails(user: User) {
        saveScrollPosition()
        val bundle = bundleOf(Pair("user_id", user.id))
        findNavController().navigate(R.id.nav_to_details, bundle)
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
        observeUsersResponse()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

}