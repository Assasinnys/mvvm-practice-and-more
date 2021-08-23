package com.example.mvvm_practice.ui.storage

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_practice.R
import com.example.mvvm_practice.databinding.FragmentStorageBinding
import com.example.mvvm_practice.extras.TAG
import com.example.mvvm_practice.extras.getStandardStringPrefs
import com.example.mvvm_practice.extras.setStandardStringPrefs
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.widget.*

class StorageFragment : Fragment() {
    // The View Binding
    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!

    // The View Model
    private val viewModel: StorageViewModel by viewModel()

    // The RecyclerView Adapter
    private var _adapter: LocalUserListAdapter? = null
    private val adapter get() = _adapter!!

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
        _adapter = adapter

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
            updateList(adapter)
        }
    }

    private fun updateList(adapter: LocalUserListAdapter) {
        val orderPreference = getStandardStringPrefs("order", "")
        adapter.submitList(
            viewModel.getOrderedAllLocalUsers(
                orderPreference?.toIntOrNull() ?: 0
            )
        )
        Log.i(TAG, "subscribeUi: orderPref: $orderPreference")
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
                //navController.navigate(R.id.action_nav_storage_to_storageSettingsFragment)
                val menuItemView = activity?.findViewById(item.itemId) as View
                showMenu(R.menu.popup_menu_filter, menuItemView)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMenu(@MenuRes menuRes: Int, anchor: View) {
        PopupMenu(requireContext(), anchor, Gravity.BOTTOM).apply {
            menuInflater.inflate(menuRes, menu)

            setOnMenuItemClickListener { menuItem: MenuItem ->
                setStandardStringPrefs("order", menuItem.order.toString())
                true
                // Respond to menu item click.
            }
            setOnDismissListener {
                updateList(adapter)
                // Respond to popup being dismissed.
            }
            // Show the popup menu.
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: storage")
        _binding = null
        _adapter = null
    }
}