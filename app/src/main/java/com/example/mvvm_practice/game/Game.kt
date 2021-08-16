package com.example.mvvm_practice.game

import GameData.Player
import GameData.GameMode
import GameData.GameState
import GameData.Companion.gameModeToInt
import GameData.Companion.indexIntoPosition
import GameData.Companion.makeEmptyGameField
import GameData.Companion.playerToCellState
import GameData.Companion.standard_game_field
import GameData.Companion.switchPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvm_practice.contains

typealias Grid = Array<Array<GameData.GameCell>>

class Game(
    private val initField: Grid = standard_game_field,
    private val initState: GameState = GameState.GAME,
    private val initMode: GameMode = GameMode.THREE_TO_THREE
) {
    private val _field = MutableLiveData(initField)
    val field: LiveData<Grid> = _field

    private val _state = MutableLiveData(initState)
    val state: LiveData<GameState> = _state

    private var currentPlayer: Player

    init {
        currentPlayer = Player.X
        updateGameState()
    }

//    fun getField() = field
//    fun getState() = state
//    fun getMode() = mode
//    fun getCurrentPlayer() = currentPlayer

    fun restart() {
        resetGameField()
        _state.value = GameState.GAME
        currentPlayer = Player.X
    }

    private fun resetGameField() {
        _field.value = makeEmptyGameField(gameModeToInt(initMode))
    }

    fun makeTurn(cellIndex: Int): Boolean {
        if (cellIndex in 0 until field.size * field.size) {
            val (row, column) = indexIntoPosition(cellIndex, field.size)
            //Acting if cell is EMPTY and game active
            return if (field[row][column].state == GameData.GameCellState.EMPTY && state == GameData.GameState.GAME) {
                println("onTurn: cellIndex: $cellIndex, currentPlayer: \"${currentPlayer}\"")
                // Saving move
                field[row][column].state = playerToCellState(currentPlayer)
                // Switching current player
                currentPlayer = switchPlayer(currentPlayer)
                updateGameState()
                true
            } else {
                println("Cell is not empty or game ended")
                false
            }
        } else {
            println("Make turn: $cellIndex out of bound ${0 until field.size * field.size}")
            return false
        }
    }

    //TODO
    //TODO scalability,
    // MANY DIAGS CHECK, IF THERE IS MORE THAN 2 DIAGONALS

    private fun hasWinState(cellToCheck: GameData.GameCellState): Boolean {
        val rowToCheck = Array(gameModeToInt(mode)) {
            GameData.GameCell(cellToCheck)
        }
        //Checking rows and columns
        for (rowIndex in field.indices) {
            //row
            if (field[rowIndex].contains(rowToCheck)) return true
            //column
            if (Array(field.size) { columnIndex ->
                    field[columnIndex][rowIndex]
                }.contains(rowToCheck)) {
                return true
            }
        }
        //Checking diagonals
        //Main diag From top to bottom
        if (Array(field.size) { index ->
                field[index][index]
            }.contains(rowToCheck)) {
            return true
        }
        //Second diag from bottom to top
        var secondDiag: Array<GameData.GameCell> = emptyArray()
        for (rowIndex in field.indices.reversed()) {
            secondDiag = secondDiag.plusElement(field[rowIndex][field.lastIndex - rowIndex])
        }
        if (secondDiag.contains(rowToCheck)) {
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
        val xWinState = hasWinState(GameData.GameCellState.CROSS)
        val oWinState = hasWinState(GameData.GameCellState.CIRCLE)
        println("X WINS: $xWinState, O WINS: $oWinState")
        //Checking isFull state
        var isFull = true
        for (row in field) {
            if (row.contains(GameData.GameCell())) {
                isFull = false
                break
            }
        }
        //Checking draw state
        if (isFull && !oWinState && !xWinState) {
            state = GameData.GameState.DRAW
            println("updateGameState: $state")
            return
        }
        //Checking impossible state
        if (oWinState && xWinState) {
            state = GameData.GameState.ERROR
            println("updateGameState: $state")
            return
        }
        var countOfX = 0
        var countOfO = 0
        field.forEach { row ->
            row.forEach { cell ->
                if (cell.state == GameData.GameCellState.CIRCLE) countOfO++
                if (cell.state == GameData.GameCellState.CROSS) countOfX++
            }
        }
        if (maxOf(countOfO, countOfX) - minOf(countOfO, countOfX) >= 2) {
            state = GameData.GameState.ERROR
            println("updateGameState: $state")
            return
        }
        //Checking win state
        state = when {
            xWinState -> GameData.GameState.X_WINS
            oWinState -> GameData.GameState.O_WINS
            else -> state
        }
        println("updateGameState: $state")
    }
}