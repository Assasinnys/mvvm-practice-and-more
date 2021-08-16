package com.example.mvvm_practice.ui.activities

import GameData.Companion.indexIntoPosition
import GameData.GameCellState
import GameData.GameState
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatImageButton
import com.example.mvvm_practice.R
import com.example.mvvm_practice.TAG
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

            initGridButtons(cells)
            viewModel.field.observe(this@MainActivity, { field ->
                updateGrid(cells, field)
            })

            viewModel.state.observe(this@MainActivity, { state ->
                stateChangedNotification(state)
            })

            viewModel.xWinsCounter.observe(this@MainActivity, { xWinsCounter ->
                updateWinStates(xWinsCounter = xWinsCounter)
            })

            viewModel.oWinsCounter.observe(this@MainActivity, { oWinsCounter ->
                updateWinStates(oWinsCounter = oWinsCounter)
            })

            restartButton.setOnClickListener {
                viewModel.startGame()
            }
        }
        //TODO implement DIFFERENT GAME TYPES: with friend, with bot. Switching by bottom nav menu
        //TODO implement SAVE GAME using LOCAL database or other local storages
    }

    private fun initGridButtons(@IdRes cells: Array<Int>) {
        cells.forEachIndexed { index, cell ->
            val button = findViewById<AppCompatImageButton>(cell)
            button.setOnClickListener {
                viewModel.makeMove(index)
            }
        }
    }

    private fun updateGrid(@IdRes cells: Array<Int>, field: field) {
        cells.forEachIndexed { index, cell ->
            val (row, column) = indexIntoPosition(index, 3)
            val button = findViewById<AppCompatImageButton>(cell)
            when (field[row][column].state) {
                GameCellState.CROSS -> {
                    button.setImageResource(R.drawable.ic_outline_cross_24)
                }
                GameCellState.CIRCLE -> {
                    button.setImageResource(R.drawable.ic_outline_circle_24)
                }
                GameCellState.EMPTY -> {
                    button.setImageResource(android.R.color.transparent)
                }
            }
        }
        Log.i(TAG, "updateGrid")
    }

    private fun stateChangedNotification(state: GameState) {
        toast?.cancel()
        toast = Toast.makeText(this, state.toString(), Toast.LENGTH_SHORT).apply { show() }
    }

    private fun updateWinStates(xWinsCounter: Int? = null, oWinsCounter: Int? = null) {
        xWinsCounter?.let { counter ->
            binding.xWinsCounter.text = resources.getString(R.string.x_wins_counter_text, counter)
        }
        oWinsCounter?.let { counter ->
            binding.oWinsCounter.text = resources.getString(R.string.o_wins_counter_text, counter)
        }
    }

    companion object {
        private var toast: Toast? = null
    }
}