package com.example.mvvm_practice.ui.activities

import GameData.Companion.indexIntoPosition
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatImageButton
import com.example.mvvm_practice.R
import com.example.mvvm_practice.viewModel.MainViewModel
import com.example.mvvm_practice.databinding.MainActivityBinding

typealias field = Array<Array<GameData.GameCell>>

class MainActivity : AppCompatActivity() {
    // The View Binding
    private lateinit var binding: MainActivityBinding

    // The View Model
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @IdRes val cells: Array<Int>
        binding.apply {
            gameField.apply {
                cells = arrayOf(
                    firstCell.id,
                    secondCell.id,
                    thirdCell.id,
                    fourthCell.id,
                    fifthCell.id,
                    sixthCell.id,
                    seventhCell.id,
                    eighthCell.id,
                    ninthCell.id
                )
            }

            initGrid(cells)
            viewModel.game.observe(this@MainActivity, { game ->
                updateGrid(cells, game.getField())
            })

            startButton.setOnClickListener {

            }

            restartButton.setOnClickListener {

            }
        }
        //TODO implement DIFFERENT GAME TYPES: with friend, with bot. Switching by bottom nav menu
        //TODO implement SAVE GAME using LOCAL database or other local storages
    }

    private fun initGrid(@IdRes cells: Array<Int>) {
        cells.forEachIndexed { index, cell ->
            val button = findViewById<AppCompatImageButton>(cell)
            button.setOnClickListener {
                viewModel.game.value?.let { game ->
                    game.makeTurn(index)
                    updateGrid(cells, game.getField())
                }
            }
        }
    }

    private fun updateGrid(@IdRes cells: Array<Int>, field: field) {
        cells.forEachIndexed { index, cell ->
            val (row, column) = indexIntoPosition(index, 3)
            val button = findViewById<AppCompatImageButton>(cell)
            when (field[row][column].state) {
                GameData.GameCellState.CROSS -> {
                    button.setImageResource(R.drawable.ic_outline_cross_24)
                }
                GameData.GameCellState.CIRCLE -> {
                    button.setImageResource(R.drawable.ic_outline_circle_24)
                }
                GameData.GameCellState.EMPTY -> {
                    button.setImageResource(android.R.color.transparent)
                }
            }
        }
    }
}