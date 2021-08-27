package com.example.mvvm_practice.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.mvvm_practice.R
import com.example.mvvm_practice.databinding.FragmentGameBinding
import com.example.mvvm_practice.extras.Grid
import com.example.mvvm_practice.extras.TAG
import com.example.mvvm_practice.gameCore.GameData.Companion.gameModeToInt
import com.example.mvvm_practice.gameCore.GameData.Companion.indexIntoPosition
import com.example.mvvm_practice.gameCore.GameData.GameCellState
import com.example.mvvm_practice.gameCore.GameData.GameState
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference

class GameFragment : Fragment() {
    // The View Binding
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    // The View Model
    private val viewModel by viewModel<GameViewModel>()

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
        subscribeUi()
    }

    private fun subscribeUi() {
        val cells: Array<ImageButton>
        binding.gameField.apply {
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

        binding.restartButton.setOnClickListener {
            viewModel.startGame()
        }

        viewModel.apply {
            field.observe(viewLifecycleOwner, { field ->
                updateGrid(cells, field)
            })

            state.observe(viewLifecycleOwner, { state ->
                stateChangedNotification(state)
            })

            xWinsCounter.observe(viewLifecycleOwner, { xWinsCounter ->
                updateWinStates(xWinsCounter = xWinsCounter)
            })

            oWinsCounter.observe(viewLifecycleOwner, { oWinsCounter ->
                updateWinStates(oWinsCounter = oWinsCounter)
            })
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
        Log.i(TAG, "game: updateGrid")
    }

    private fun stateChangedNotification(state: GameState) {
        snackbar?.clear()
        snackbar = WeakReference(
            view?.let {
                Snackbar.make(it, state.toString(), Snackbar.LENGTH_SHORT)
                    .apply {
                        setAction("Cancel") {}
                        show()
                    }
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
    }
}