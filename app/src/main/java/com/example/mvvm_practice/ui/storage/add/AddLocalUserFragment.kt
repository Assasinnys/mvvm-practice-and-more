package com.example.mvvm_practice.ui.storage.add

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import com.example.mvvm_practice.R
import com.example.mvvm_practice.databinding.FragmentAddLocalUserBinding
import com.example.mvvm_practice.extra.TAG
import com.example.mvvm_practice.extra.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddLocalUserFragment : Fragment() {

    // The View Binding
    private var _binding: FragmentAddLocalUserBinding? = null
    private val binding get() = _binding!!

    // The View Model
    private val viewModel by viewModel<AddLocalUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAddLocalUserBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            view.setOnClickListener {
                textFieldNickname.apply {
                    clearFocus()
                    error = null
                }
                textFieldFirstName.clearFocus()
                textFieldSecondName.clearFocus()
                textFieldAge.clearFocus()
                hideKeyboard()
            }

            textFieldNickname.apply {
                editText?.addTextChangedListener {
                    error = null
                }
            }

            (textFieldAge.editText as? AutoCompleteTextView)?.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.list_item,
                    viewModel.ageAutoCompleteTextViewItems
                )
            )

            initAddUserButton()
        }
    }

    private fun initAddUserButton() {
        binding.apply {
            var isClickSuccessful: Boolean
            buttonAddUser.setOnClickListener {
                isClickSuccessful = viewModel.addUser(
                    textFieldNickname.editText?.text,
                    textFieldFirstName.editText?.text,
                    textFieldSecondName.editText?.text,
                    textFieldAge.editText?.text
                )
                if (isClickSuccessful) {
                    it.isClickable = false
                    activity?.onBackPressed()
                } else {
                    // Set error text
                    textFieldNickname.error = getString(R.string.nickname_input_error)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView")
        _binding = null
    }
}