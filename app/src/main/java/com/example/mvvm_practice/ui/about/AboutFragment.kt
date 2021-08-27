package com.example.mvvm_practice.ui.about

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mvvm_practice.databinding.FragmentAboutBinding
import com.example.mvvm_practice.extras.TAG
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : Fragment() {
    // The View Binding
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    // The View Model
    private val viewModel by viewModel<AboutViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAboutBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textview.text = viewModel.getTextAboutApp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: about app")
        _binding = null
    }
}