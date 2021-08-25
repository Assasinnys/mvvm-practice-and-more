package com.example.mvvm_practice.ui.storage.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.mvvm_practice.R
import com.example.mvvm_practice.databinding.FragmentAddLocalUserBinding
import com.example.mvvm_practice.extras.INVALID_VALUE
import com.example.mvvm_practice.extras.TAG
import com.example.mvvm_practice.extras.hideKeyboard
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

        initInputFields()
        initAddUserButton()

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
        }
    }

    private fun initInputFields() {
        arguments?.apply {
            val nickname: String = getString("nickname", "")
            val firstName = getString("first_name", "")
            val secondName = getString("second_name", "")
            val age: Int = getInt("age", INVALID_VALUE)

            binding.apply {
                textFieldNickname.editText?.setText(nickname)
                textFieldFirstName.editText?.setText(firstName)
                textFieldSecondName.editText?.setText(secondName)
                textFieldAge.editText?.setText(age.toString())
            }
        }
    }

    private fun initAddUserButton() {
        var id: Int? = arguments?.getInt("id", INVALID_VALUE)
        if (id == INVALID_VALUE) id = null
        //if id passed as argument to add user fragment, then update existing user
        binding.apply {
            var isClickSuccessful: Boolean
            buttonAddUser.setOnClickListener {
                isClickSuccessful = viewModel.addUser(
                    textFieldNickname.editText?.text,
                    textFieldFirstName.editText?.text,
                    textFieldSecondName.editText?.text,
                    textFieldAge.editText?.text,
                    id
                )
                if (isClickSuccessful) {
                    it.isClickable = false
                    hideKeyboard()
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
        Log.i(TAG, "onDestroyView: add user")
        _binding = null
    }
}