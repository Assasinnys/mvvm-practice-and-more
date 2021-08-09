package com.example.mvvm_practice

import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.example.mvvm_practice.game.GameData

const val TAG = "GAME"

fun ImageButton.setOnCellStateImage(state: GameData.GameCellState) = when (state) {
    GameData.GameCellState.EMPTY -> {
        this.setImageResource(android.R.color.transparent)
    }
    GameData.GameCellState.CIRCLE -> {
        this.setImageDrawable(
            ContextCompat.getDrawable(
                this.context,
                R.drawable.ic_outline_circle_24
            )
        )
    }
    GameData.GameCellState.CROSS -> {
        this.imageTintMode
        this.setImageDrawable(
            ContextCompat.getDrawable(
                this.context,
                R.drawable.ic_outline_cross_24
            )
        )
    }
}