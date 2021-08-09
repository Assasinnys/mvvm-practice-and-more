package com.example.mvvm_practice.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatImageButton
import com.example.mvvm_practice.TAG
import com.example.mvvm_practice.viewModel.MainViewModel
import com.example.mvvm_practice.databinding.MainActivityBinding
import com.example.mvvm_practice.game.GameLogic

class MainActivity : AppCompatActivity() {
    // The View Binding
    private var _binding: MainActivityBinding? = null
    private val binding get() = _binding!!

    // The View Model
    private val mainViewModel: MainViewModel by viewModels()

    // The GameLogic handler
    private var _gameLogic: GameLogic? = null
    private val gameLogic get() = _gameLogic!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            startGame(this@MainActivity, mainViewModel, cells)

            restartButton.setOnClickListener {
                gameLogic.resetGameInfo()
            }
        }
        //TODO implement DIFFERENT GAME TYPES: with friend, with bot. Switching by bottom nav menu
        //TODO implement SAVE GAME using LOCAL database or other local storages
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d(TAG, "onDestroy")
        if (isFinishing) {
            Log.d(TAG, "onDestroy: onFinishing")
            _gameLogic = null
        }
    }

    private fun startGame(
        context: Context,
        viewModel: MainViewModel,
        cells: Array<AppCompatImageButton>
    ) {
        // observe the Live Data in View Model
        mainViewModel.gameInfo.observe(this, {
            Log.d(TAG, "OBSERVE_GAME !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            _gameLogic = GameLogic(context, viewModel, cells)
        })
    }
}