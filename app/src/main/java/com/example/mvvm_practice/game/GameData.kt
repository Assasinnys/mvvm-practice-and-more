package com.example.mvvm_practice.game

class GameData {
    enum class Player {
        O, X
    }

    enum class GameCellState {
        EMPTY, CIRCLE, CROSS
    }

    enum class GameState {
        GAME, X_WINS, O_WINS, DRAW, ERROR
    }

    enum class GameMode {
        THREE_TO_THREE, FIVE_TO_FIVE
    }

    data class GameCell(
        var state: GameCellState = GameCellState.EMPTY
    )

    companion object {
        fun playerToCellState(player: Player): GameCellState =
            if (player == Player.X) GameCellState.CROSS else GameCellState.CIRCLE

        fun gameModeToInt(mode: GameMode): Int = when (mode) {
            GameMode.THREE_TO_THREE -> 3
            GameMode.FIVE_TO_FIVE -> 5
        }

        fun switchPlayer(player: Player): Player = if (player == Player.X) Player.O else Player.X

        fun indexIntoPosition(index: Int, cellsInRow: Int) = Pair(index / cellsInRow, index % cellsInRow)

        fun positionIntoIndex(position: Pair<Int, Int>, cellsInRow: Int) = position.first * cellsInRow + position.second

        fun makeEmptyGameField(size: Int) = Array(size) {
            Array(size) {
                GameCell()
            }
        }

        val standard_game_state get() = GameState.GAME
        val standard_current_player get() = Player.X
        val standard_game_field get() = makeEmptyGameField(3)
    }
}