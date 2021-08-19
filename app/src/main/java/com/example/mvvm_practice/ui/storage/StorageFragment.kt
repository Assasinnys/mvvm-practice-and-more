package com.example.mvvm_practice.ui.storage

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_practice.MyApplication
import com.example.mvvm_practice.R
import com.example.mvvm_practice.databinding.FragmentAboutBinding
import com.example.mvvm_practice.databinding.FragmentStorageBinding
import com.example.mvvm_practice.extra.TAG
import com.example.mvvm_practice.ui.about.AboutViewModel
import com.example.mvvm_practice.ui.storage.model.LocalUser
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

        val adapter = LocalUserListAdapter()
        val navController = findNavController()

        binding.apply {
            viewModel.allLocalUsers.observe(viewLifecycleOwner, { users ->
                users?.let { list ->
                    adapter.submitList(list /*viewModel.sortedList(list)*/)
                    Log.i(TAG, "list observe ${activity?.getSharedPreferences(context?.packageName + "_preferences", Context.MODE_PRIVATE)?.getString("order", "227")}")
                }
            })
            /*TODO
             * В адаптер ресайклера отправить DAO, или сам адаптер сделать синглотоном в модуле коина.
             * Все настройки при СОЗДАНИИ и ИЗМЕНЕНИИ отправлять в главную вьюмодель. activity.viewModel.updateSomeSetting(setting) также и забирать, например,
             * отобразить список базы данных.
             * Главную вьюмодель через koin inject'нуть.
             *
             * TODO ИСПРАВИТЬ КОСТЫЛИ С ФИЛЬТРОМ В НАСТРОЙКАХ STORAGE
             */

            viewModel.orderBy.observe(viewLifecycleOwner, {
                Log.i(TAG, "orderBy observer: $it")
            })

            recyclerview.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(context)
            }

            fabFilterUsers.setOnClickListener {
                navController.navigate(R.id.action_nav_storage_to_storageSettingsFragment)
            }

            fabAddUser.setOnClickListener {
                navController.navigate(R.id.action_nav_storage_to_addLocalUserFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView")
        _binding = null
    }
}