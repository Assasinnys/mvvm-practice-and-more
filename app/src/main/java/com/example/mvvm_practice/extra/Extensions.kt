package com.example.mvvm_practice.extra

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.mvvm_practice.gameCore.GameData

const val TAG = "GAME"

fun Fragment.hideKeyboard() = view?.let { activity?.hideKeyboard(it) }

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Array<Array<GameData.GameCell>>.print() {
    val ansiGreen = "\u001B[32m"
    val ansiRed = "\u001B[31m"
    val ansiResetColor = "\u001B[0m"
    println("Current field: ")
    this.forEach { row ->
        row.forEach { cell ->
            when (cell.state) {
                GameData.GameCellState.CROSS -> {
                    print(ansiRed + "${cell.state} " + ansiResetColor)
                }
                GameData.GameCellState.CIRCLE -> {
                    print(ansiGreen + "${cell.state} " + ansiResetColor)
                }
                else -> {
                    print("${cell.state} ")
                }
            }
        }
        println()
    }
}

fun <T> Array<T>.contains(other: Array<T>): Boolean {
    var contains = true
    for (arrIndex in 0..(this.lastIndex - other.lastIndex)) {
        for (arrIndex2 in other.indices) {
            if (this[arrIndex + arrIndex2] != other[arrIndex2]) {
                contains = false
                break
            }
        }
        if (contains) return true else contains = true
    }
    return false
}

fun Array<*>.containsStartAt(other: Array<*>): Int? {
    var contains = true
    for (arrIndex in 0..(this.lastIndex - other.lastIndex)) {
        for (arrIndex2 in other.indices) {
            if (this[arrIndex + arrIndex2] != other[arrIndex2]) {
                contains = false
                break
            }
        }
        if (contains) {
            return arrIndex
        } else contains = true
    }
    return null
}