package com.example.mvvm_practice.game

sealed class GameData {
    enum class Player {
        O, X
    }

    enum class GameCellState {
        EMPTY, CIRCLE, CROSS
    }

    enum class GameState {
        GAME, X_WINS, O_WINS, DRAW, ERROR
    }

    data class GameCell(
        var state: GameCellState = GameCellState.EMPTY
    )

    companion object {
        fun playerCellState(player: Player): GameCellState = if (player == Player.X) GameCellState.CROSS else GameCellState.CIRCLE
        fun switchPlayer(player: Player): Player = if (player == Player.X) Player.O else Player.X

        val CellsIndexPositions: Array<Pair<Int, Int>>
            get() {
                return arrayOf(
                    Pair(0, 0),
                    Pair(0, 1),
                    Pair(0, 2),
                    Pair(1, 0),
                    Pair(1, 1),
                    Pair(1, 2),
                    Pair(2, 0),
                    Pair(2, 1),
                    Pair(2, 2)
                )
            }

        val standard_game_state get() = GameState.GAME
        val standard_current_player get() = Player.X
        val standard_game_field
            get() = arrayOf(
                arrayOf(GameCell(), GameCell(), GameCell()),
                arrayOf(GameCell(), GameCell(), GameCell()),
                arrayOf(GameCell(), GameCell(), GameCell()),
            )
    }
}