package com.example.mvvm_practice.ui.storage

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_practice.R
import com.example.mvvm_practice.databinding.FragmentStorageBinding
import com.example.mvvm_practice.extras.TAG
import com.example.mvvm_practice.extras.getStandardStringPrefs
import org.koin.androidx.viewmodel.ext.android.viewModel

class StorageFragment : Fragment() {
    // The View Binding
    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!

    // The View Model
    private val viewModel: StorageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentStorageBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val navController = findNavController()

        val adapter = LocalUserListAdapter { userToEdit ->
            userToEdit.apply {
                navController.navigate(
                    R.id.action_nav_storage_to_addLocalUserFragment,
                    bundleOf(
                        "id" to id,
                        "nickname" to nickname,
                        "first_name" to firstName,
                        "second_name" to secondName,
                        "age" to age
                    )
                )
            }
        }

        subscribeUi(adapter)

        binding.apply {
            recyclerview.apply {
                this.adapter = adapter
                deleteRecyclerViewItemCallback().attachToRecyclerView(this)
            }
        }
    }

    private fun subscribeUi(adapter: LocalUserListAdapter) {
        viewModel.allLocalUsers.observe(viewLifecycleOwner) {
            val orderPreference = getStandardStringPrefs("order", "")
            adapter.submitList(
                viewModel.getOrderedAllLocalUsers(
                    orderPreference?.toIntOrNull() ?: 0
                )
            )
            Log.i(TAG, "subscribeUi: orderPref: $orderPreference")
        }
    }

    private fun deleteRecyclerViewItemCallback() =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val userId =
                    viewHolder.itemView.findViewById<TextView>(R.id.user_id).text.toString()
                        .substringAfter("id: ").trim()
                        .toIntOrNull()
                Log.i(TAG, "onSwiped: userId: $userId")
                userId?.let {
                    viewModel.deleteById(userId)
                }
            }
        })

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.navigation_storage, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController()
        return when (item.itemId) {
            R.id.nav_about_storage -> {
                navController.navigate(R.id.action_nav_storage_to_aboutStorageFragment)
                true
            }
            R.id.nav_add_local_user -> {
                navController.navigate(R.id.action_nav_storage_to_addLocalUserFragment)
                true
            }
            R.id.nav_filter_storage -> {
                navController.navigate(R.id.action_nav_storage_to_storageSettingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: storage")
        _binding = null
    }
}