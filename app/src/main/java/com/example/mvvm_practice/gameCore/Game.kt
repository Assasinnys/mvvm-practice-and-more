package com.example.mvvm_practice.gameCore

import com.example.mvvm_practice.gameCore.GameData.Player
import com.example.mvvm_practice.gameCore.GameData.GameCellState
import com.example.mvvm_practice.gameCore.GameData.GameMode
import com.example.mvvm_practice.gameCore.GameData.GameState
import com.example.mvvm_practice.gameCore.GameData.Companion.gameModeToInt
import com.example.mvvm_practice.gameCore.GameData.Companion.indexIntoPosition
import com.example.mvvm_practice.gameCore.GameData.Companion.makeEmptyGameField
import com.example.mvvm_practice.gameCore.GameData.Companion.playerToCellState
import com.example.mvvm_practice.gameCore.GameData.Companion.standard_game_field
import com.example.mvvm_practice.gameCore.GameData.Companion.switchPlayer
import androidx.lifecycle.LiveData
import com.example.mvvm_practice.extras.Grid
import com.example.mvvm_practice.extras.NotNullMutableLiveData
import com.example.mvvm_practice.extras.contains

class Game(
    initField: Grid = standard_game_field,
    initState: GameState = GameState.GAME,
    val initMode: GameMode = GameMode.THREE_TO_THREE
) {
    private val _field = NotNullMutableLiveData(initField)
    val field: LiveData<Grid> = _field

    private val _state = NotNullMutableLiveData(initState)
    val state: LiveData<GameState> = _state

    private val _xWinsCounter = NotNullMutableLiveData(0)
    val xWinsCounter: LiveData<Int> = _xWinsCounter

    private val _oWinsCounter = NotNullMutableLiveData(0)
    val oWinsCounter: LiveData<Int> = _oWinsCounter

    private val _currentPlayer = NotNullMutableLiveData(Player.X)
    val currentPlayer: LiveData<Player> = _currentPlayer

    init {
        restart()
    }

    fun restart() {
        _field.value = makeEmptyGameField(gameModeToInt(initMode))
        _state.value = GameState.GAME
        _currentPlayer.value = Player.X
    }

    fun makeTurn(cellIndex: Int): Boolean {
        if (cellIndex in 0 until _field.value.size * _field.value.size) {
            val (row, column) = indexIntoPosition(cellIndex, _field.value.size)
            //Acting if cell is EMPTY and game active
            return if (_field.value[row][column].state == GameCellState.EMPTY && state.value == GameState.GAME) {
                println("onTurn: cellIndex: $cellIndex, currentPlayer: \"${currentPlayer.value}\"")
                // Saving move
                _field.value[row][column].state = playerToCellState(_currentPlayer.value)
                // Switching current player
                _currentPlayer.value = switchPlayer(_currentPlayer.value)
                updateGameState()
                _field.value = _field.value
                true
            } else {
                println("Cell is not empty or game ended")
                false
            }
        } else {
            println("Make turn: $cellIndex out of bound ${0 until _field.value.size * _field.value.size}")
            return false
        }
    }

    //TODO
    //TODO scalability,
    // MANY DIAGONALS CHECK, IF THERE IS MORE THAN 2 DIAGONALS

    private fun hasWinState(cellToCheck: GameCellState): Boolean {
        val rowToCheck = Array(gameModeToInt(initMode)) {
            GameData.GameCell(cellToCheck)
        }
        //Checking rows and columns
        for (rowIndex in _field.value.indices) {
            //row
            if (_field.value[rowIndex].contains(rowToCheck)) return true
            //column
            if (Array(_field.value.size) { columnIndex ->
                    _field.value[columnIndex][rowIndex]
                }.contains(rowToCheck)) {
                return true
            }
        }
        //Checking diagonals
        //Main diagonals From top to bottom
        if (Array(_field.value.size) { index ->
                _field.value[index][index]
            }.contains(rowToCheck)) {
            return true
        }
        //Second diagonals from bottom to top
        var secondDiagonal: Array<GameData.GameCell> = emptyArray()
        for (rowIndex in _field.value.indices.reversed()) {
            secondDiagonal =
                secondDiagonal.plusElement(_field.value[rowIndex][_field.value.lastIndex - rowIndex])
        }
        if (secondDiagonal.contains(rowToCheck)) {
            return true
        }
        //If no three in a row, then return false
        return false
    }

    //TODO
    //TODO
    //TODO

    private fun updateGameState() {
        //Checking X or O win
        val xWinState = hasWinState(GameCellState.CROSS)
        val oWinState = hasWinState(GameCellState.CIRCLE)
        println("X WINS: $xWinState, O WINS: $oWinState")
        //Checking isFull state
        var isFull = true
        for (row in field.value!!) {
            if (row.contains(GameData.GameCell())) {
                isFull = false
                break
            }
        }
        //Checking draw state
        if (isFull && !oWinState && !xWinState) {
            _state.value = GameState.DRAW
            println("updateGameState: ${state.value}")
            return
        }
        //Checking impossible state
        if (oWinState && xWinState) {
            _state.value = GameState.ERROR
            println("updateGameState: ${state.value}")
            return
        }
        var countOfX = 0
        var countOfO = 0
        field.value?.forEach { row ->
            row.forEach { cell ->
                if (cell.state == GameCellState.CIRCLE) countOfO++
                if (cell.state == GameCellState.CROSS) countOfX++
            }
        }
        if (maxOf(countOfO, countOfX) - minOf(countOfO, countOfX) >= 2) {
            _state.value = GameState.ERROR
            println("updateGameState: ${state.value}")
            return
        }
        //Checking win state
        when {
            xWinState -> {
                _state.value = GameState.X_WINS
                _xWinsCounter.value += 1
            }
            oWinState -> {
                _state.value = GameState.O_WINS
                _oWinsCounter.value += 1
            }
        }
        println("updateGameState: ${state.value}")
    }
}