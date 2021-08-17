package com.example.mvvm_practice.game.bot

sealed class GameBotData {
    enum class Mode {
        EASY, MEDIUM, HARD
    }
}
