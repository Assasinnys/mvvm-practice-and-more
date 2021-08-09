package com.example.mvvm_practice.model

import com.example.mvvm_practice.game.GameData

data class GameInfo(
    var game_state: GameData.GameState,
    var current_player: GameData.Player,
    var game_field: Array<Array<GameData.GameCell>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameInfo

        if (game_state != other.game_state) return false
        if (current_player != other.current_player) return false
        if (!game_field.contentDeepEquals(other.game_field)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = game_state.hashCode()
        result = 31 * result + current_player.hashCode()
        result = 31 * result + game_field.contentDeepHashCode()
        return result
    }

    companion object {
        fun getStandardGameInfo() = GameInfo(
            game_state = GameData.standard_game_state,
            game_field = GameData.standard_game_field,
            current_player = GameData.standard_current_player
        )
    }
}