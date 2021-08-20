package com.example.mvvm_practice.ui.game

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import com.example.mvvm_practice.gameCore.GameData.Companion.gameModeToInt
import androidx.fragment.app.viewModels
import com.example.mvvm_practice.extra.Grid
import com.example.mvvm_practice.gameCore.GameData.Companion.indexIntoPosition
import com.example.mvvm_practice.R
import com.example.mvvm_practice.extra.TAG
import com.example.mvvm_practice.databinding.FragmentGameBinding
import com.example.mvvm_practice.extra.resetOrientation
import com.example.mvvm_practice.extra.setPortraitOrientation
import com.example.mvvm_practice.gameCore.GameData.GameState
import com.example.mvvm_practice.gameCore.GameData.GameCellState
import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference

class GameFragment : Fragment() {
    // The View Binding
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    // The View Model
    private val viewModel by viewModels<GameViewModel>()

    // The WeakReference Snack bar
    private var snackbar: WeakReference<Snackbar>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentGameBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPortraitOrientation()

        binding.apply {
            val cells: Array<ImageButton>
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

            //initGridButtons
            cells.forEachIndexed { index, button ->
                button.setOnClickListener {
                    viewModel.makeMove(index)
                }
            }

            viewModel.field.observe(viewLifecycleOwner, { field ->
                updateGrid(cells, field)
            })

            viewModel.state.observe(viewLifecycleOwner, { state ->
                stateChangedNotification(state)
            })

            viewModel.xWinsCounter.observe(viewLifecycleOwner, { xWinsCounter ->
                updateWinStates(xWinsCounter = xWinsCounter)
            })

            viewModel.oWinsCounter.observe(viewLifecycleOwner, { oWinsCounter ->
                updateWinStates(oWinsCounter = oWinsCounter)
            })

            restartButton.setOnClickListener {
                viewModel.startGame()
            }
        }
        //TODO implement DIFFERENT GAME TYPES: with friend, with bot. Switching by bottom nav menu
        //TODO implement SAVE GAME using LOCAL database or other local storages
    }

    private fun stateChangedNotification(state: GameState) {
        snackbar?.clear()
        snackbar = WeakReference(
            view?.let {
                Snackbar.make(it, state.toString(), Snackbar.LENGTH_SHORT).apply { show() }
            }
        )
    }

    private fun updateWinStates(xWinsCounter: Int? = null, oWinsCounter: Int? = null) {
        xWinsCounter?.let { counter ->
            binding.xWinsCounter.text = resources.getString(R.string.x_wins_counter_text, counter)
        }
        oWinsCounter?.let { counter ->
            binding.oWinsCounter.text = resources.getString(R.string.o_wins_counter_text, counter)
        }
    }

    private fun updateGrid(cells: Array<ImageButton>, field: Grid) {
        val cellsInRow = gameModeToInt(viewModel.getInitMode())
        cells.forEachIndexed { index, button ->
            val (row, column) = indexIntoPosition(index, cellsInRow)
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

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: game")
        snackbar?.clear()
        snackbar = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: game")
        _binding = null
        snackbar?.clear()
        snackbar = null
        resetOrientation()
    }
}