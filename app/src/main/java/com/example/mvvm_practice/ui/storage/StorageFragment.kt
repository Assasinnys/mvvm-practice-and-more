package com.example.mvvm_practice.ui.storage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm_practice.R
import com.example.mvvm_practice.databinding.FragmentStorageBinding
import com.example.mvvm_practice.extra.TAG
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class StorageFragment : Fragment() {
    // The View Binding
    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!

    // The View Model
    private val viewModel: StorageViewModel by viewModel()

    //// The View Model
    //    private val viewModel: StorageViewModel by viewModels {
    //        StorageViewModelFactory((activity?.application as MyApplication).repository)
    //    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentStorageBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val adapter = LocalUserListAdapter(get())

        subscribeUi(adapter)

        binding.apply {
            recyclerview.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private fun subscribeUi(adapter: LocalUserListAdapter) {
        viewModel.allLocalUsers.observe(viewLifecycleOwner) {
            val orderPreference = activity?.getSharedPreferences(context?.packageName + "_preferences", Context.MODE_PRIVATE)?.getString("order", "")
            adapter.submitList(viewModel.getOrderedAllLocalUsers(orderPreference?.toIntOrNull() ?: 0))
            Log.i(TAG, "subscribeUi: orderPref: $orderPreference")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.navigation_storage, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController()
        return when (item.itemId) {
            R.id.nav_about_storage -> {
                // navigate to about settings screen
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