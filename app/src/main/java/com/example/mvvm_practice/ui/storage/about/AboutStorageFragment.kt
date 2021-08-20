package com.example.mvvm_practice.ui.storage.about

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mvvm_practice.databinding.FragmentAboutStorageBinding
import com.example.mvvm_practice.extra.TAG
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutStorageFragment : Fragment() {
    // The View Binding
    private var _binding: FragmentAboutStorageBinding? = null
    private val binding get() = _binding!!

    // The View Model
    private val viewModel by viewModel<AboutStorageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAboutStorageBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textview.text = viewModel.getTextAboutStorage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView")
        _binding = null
    }
}