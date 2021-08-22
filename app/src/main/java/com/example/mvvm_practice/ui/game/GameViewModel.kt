package com.example.mvvm_practice.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_practice.extras.Grid
import com.example.mvvm_practice.gameCore.Game
import com.example.mvvm_practice.gameCore.GameData

class GameViewModel : ViewModel() {
    private val game = Game()

    val field: LiveData<Grid> = game.field

    val state: LiveData<GameData.GameState> = game.state

    val currentPlayer: LiveData<GameData.Player> = game.currentPlayer

    val xWinsCounter: LiveData<Int> = game.xWinsCounter

    val oWinsCounter: LiveData<Int> = game.oWinsCounter

    fun startGame() = game.restart()

    fun makeMove(index: Int): Boolean = game.makeTurn(index)

    fun getInitMode() = game.initMode
}