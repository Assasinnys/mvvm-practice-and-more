package com.example.mvvm_practice.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.viewModels
import com.example.mvvm_practice.databinding.FragmentGameBinding
import com.example.mvvm_practice.viewModel.MainViewModel

class GameFragment : Fragment() {
    // The View Binding
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    // The View Model
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentGameBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cells: Array<AppCompatImageButton>
        binding.apply {
            gameField.apply {
                        cells = arrayOf(
                            firstCell,
                            secondCell,
                            thirdCell,
                            fourthCell,
                            fifthCell,
                            sixthCell,
                            seventhCell,
                            eighthCell,
                            ninthCell
                        )
                    }

            restartButton.setOnClickListener {

            }
        }

        //TODO observe game
        viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}