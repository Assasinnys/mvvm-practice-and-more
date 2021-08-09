package com.example.mvvm_practice.game

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.lifecycle.LifecycleOwner
import com.example.mvvm_practice.TAG
import com.example.mvvm_practice.game.GameData.Companion.CellsIndexPositions
import com.example.mvvm_practice.game.GameData.Companion.playerCellState
import com.example.mvvm_practice.game.GameData.Companion.standard_current_player
import com.example.mvvm_practice.game.GameData.Companion.standard_game_field
import com.example.mvvm_practice.game.GameData.Companion.standard_game_state
import com.example.mvvm_practice.game.GameData.Companion.switchPlayer
import com.example.mvvm_practice.model.*
import com.example.mvvm_practice.setOnCellStateImage
import com.example.mvvm_practice.viewModel.MainViewModel
import kotlin.math.max
import kotlin.math.min

class GameLogic(
    private val context: Context,
    private val gameViewModel: MainViewModel,
    private val cells: Array<AppCompatImageButton>
) {
    init {
        checkGameState()
        initButtons()
    }

    private fun initButtons() {
        gameViewModel.gameInfo.value?.apply {
            for (cellIndex in cells.indices) {
                val (row, column) = Pair(
                    CellsIndexPositions[cellIndex].first,
                    CellsIndexPositions[cellIndex].second
                )
                cells[cellIndex].setOnClickListener {
                    makeTurn(cellIndex)
                }
                cells[cellIndex].setOnCellStateImage(game_field[row][column].state)
            }
            updateAllButtonsStates()
        }
    }

    private fun hasThreeInRow(
        game_field: Array<Array<GameData.GameCell>>,
        cellToCheck: GameData.GameCellState
    ): Boolean {
        val threeInRow = arrayOf(GameData.GameCell(cellToCheck), GameData.GameCell(cellToCheck), GameData.GameCell(cellToCheck))
        //Checking rows and columns
        for (row in 0..2) {
            if (game_field[row].contentEquals(threeInRow)) return true
            if (arrayOf(game_field[0][row], game_field[1][row], game_field[2][row]).contentEquals(
                    threeInRow
                )
            ) return true
        }

        //Checking diagonals
        if (arrayOf(
                game_field[0][0],
                game_field[1][1],
                game_field[2][2]
            ).contentEquals(threeInRow)
        ) return true
        if (arrayOf(
                game_field[0][2],
                game_field[1][1],
                game_field[2][0]
            ).contentEquals(threeInRow)
        ) return true
        //If no three in a row, then return false
        return false
    }

    private fun updateGameState() {
        gameViewModel.gameInfo.value?.apply {
            //Checking X or O win
            val isXwins = hasThreeInRow(game_field, GameData.GameCellState.CROSS)
            val isOwins = hasThreeInRow(game_field, GameData.GameCellState.CIRCLE)
            Log.d(TAG, "X WINS: $isXwins, O WINS: $isOwins")

            //Checking is there empty cell
            var isFull = true
            for (row in 0..2) if (game_field[row].contains(GameData.GameCell())) isFull = false

            //Checking draw state
            if (isFull && !isOwins && !isXwins) {
                game_state = GameData.GameState.DRAW
                Log.d(TAG, "updateGameState: $game_state")
                return
            }
            //Checking impossible state
            if (isOwins && isXwins) {
                game_state = GameData.GameState.ERROR
                Log.d(TAG, "updateGameState: $game_state")
                return
            }
            var countOfX = 0
            var countOfO = 0
            game_field.forEach { row ->
                row.forEach { gameCell ->
                    if (gameCell.state == GameData.GameCellState.CIRCLE) countOfO++
                    if (gameCell.state == GameData.GameCellState.CROSS) countOfX++
                }
            }
            if (max(countOfO, countOfX) - min(countOfO, countOfX) >= 2) {
                game_state = GameData.GameState.ERROR
                Log.d(TAG, "updateGameState: $game_state")
                return
            }
            //Checking win state
            if (isXwins) {
                game_state = GameData.GameState.X_WINS
            }
            if (isOwins) {
                game_state = GameData.GameState.O_WINS
            }

            Log.d(TAG, "updateGameState: $game_state")
        }
    }

    private fun updateButtonState(cellIndex: Int) {
        if (cellIndex in cells.indices) {
            gameViewModel.gameInfo.value?.apply {
                cells[cellIndex].setOnCellStateImage(game_field[CellsIndexPositions[cellIndex].first][CellsIndexPositions[cellIndex].second].state)
            }
        }
    }

    private fun updateAllButtonsStates() {
        gameViewModel.gameInfo.value?.apply {
            for (cellIndex in cells.indices) {
                cells[cellIndex].setOnCellStateImage(game_field[CellsIndexPositions[cellIndex].first][CellsIndexPositions[cellIndex].second].state)
            }
        }
    }

    private fun checkGameState() {
        gameViewModel.gameInfo.value?.apply {
            if (game_state != GameData.GameState.GAME) {
                for (cell in cells) {
                    cell.isClickable = false
                }
                //ERROR state of game
                if (game_state == GameData.GameState.ERROR) {
                    Toast.makeText(context, "ERROR state", Toast.LENGTH_LONG).show()
                }
                //WIN or DRAW state
                Toast.makeText(context, "$game_state", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "END OF GAME: $game_state")
            } else {
                for (cell in cells) {
                    cell.isClickable = true
                }
            }
        }
    }

    private fun makeTurn(cellIndex: Int) {
        if (cellIndex in cells.indices) {
            gameViewModel.gameInfo.value?.apply {
                val (row, column) = Pair(
                    CellsIndexPositions[cellIndex].first,
                    CellsIndexPositions[cellIndex].second
                )

                //Acting if cell is EMPTY
                if (game_field[row][column].state == GameData.GameCellState.EMPTY && game_state == GameData.GameState.GAME) {
                    Log.d("GAME", "onTurn: cellIndex: $cellIndex, currentPlayer: \"${current_player}\"\n")
                    // Saving move
                    game_field[row][column].state = playerCellState(current_player)
                    // Switching current player
                    current_player = switchPlayer(current_player)
                    updateButtonState(cellIndex)
                    updateGameState()
                    checkGameState()
                }
            }
        }
    }

    fun resetGameInfo() {
        gameViewModel.gameInfo.value?.let { game_info ->
            game_info.apply {
                game_state = standard_game_state
                current_player = standard_current_player
                game_field = standard_game_field
            }
        }
        updateAllButtonsStates()
        updateGameState()
        checkGameState()
    }
}