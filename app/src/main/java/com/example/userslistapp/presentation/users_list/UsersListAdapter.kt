package com.example.userslistapp.presentation.users_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.userslistapp.databinding.UserItemBinding
import com.example.userslistapp.presentation.model.User

class UsersListAdapter(
    private val onUserClick: (User) -> Unit
) : RecyclerView.Adapter<UsersListAdapter.UsersViewHolder>() {
    private var users: List<User> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }

    class UsersViewHolder(
        private val onUseClick: (User) -> Unit,
        private val binding: UserItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.textViewId.text = user.id.toString()
            binding.textViewName.text = user.name
            binding.textViewEmail.text = user.email
            binding.root.setOnClickListener {
                onUseClick(user)
            }
        }
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(onUserClick, binding)
    }

    override fun getItemCount(): Int {
        return users.size
    }

}