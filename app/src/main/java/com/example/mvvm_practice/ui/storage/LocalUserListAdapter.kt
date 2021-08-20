package com.example.mvvm_practice.ui.storage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_practice.R
import com.example.mvvm_practice.data.LocalUser
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LocalUserListAdapter(private val viewModel: StorageViewModel) :
    ListAdapter<LocalUser, LocalUserListAdapter.LocalUserViewHolder>(LocalUsersComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalUserViewHolder {
        return LocalUserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: LocalUserViewHolder, position: Int) {
        getItem(position).apply {
            holder.bind(id, nickname, firstName, secondName, age)
        }
        holder.deleteItemView.setOnClickListener {
            viewModel.delete(getItem(position))
        }
    }

    class LocalUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val idItemView: TextView = itemView.findViewById(R.id.user_id)
        private val nicknameItemView: TextView = itemView.findViewById(R.id.nickname)
        private val firstNameItemView: TextView = itemView.findViewById(R.id.first_name)
        private val secondNameItemView: TextView = itemView.findViewById(R.id.second_name)
        private val ageItemView: TextView = itemView.findViewById(R.id.age)
        val deleteItemView: FloatingActionButton = itemView.findViewById(R.id.delete_user)

        fun bind(id: Int, nickname: String, firstName: String?, secondName: String?, age: Int?) {
            idItemView.text = "id: $id"
            nicknameItemView.text = "Nickname: $nickname"
            firstNameItemView.text = "First name: $firstName"
            secondNameItemView.text = "Second name: $secondName"
            ageItemView.text = "Age: $age"
        }

        companion object {
            fun create(parent: ViewGroup): LocalUserViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return LocalUserViewHolder(view)
            }
        }
    }

    class LocalUsersComparator : DiffUtil.ItemCallback<LocalUser>() {
        override fun areItemsTheSame(oldItem: LocalUser, newItem: LocalUser): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: LocalUser, newItem: LocalUser): Boolean {
            return oldItem.nickname == newItem.nickname && oldItem.firstName == newItem.firstName && oldItem.secondName == newItem.secondName && oldItem.age == newItem.age
        }
    }
}