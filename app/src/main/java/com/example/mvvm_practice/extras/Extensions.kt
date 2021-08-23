package com.example.mvvm_practice.extras

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.mvvm_practice.R
import com.example.mvvm_practice.gameCore.GameData

fun Fragment.setPortraitOrientation() {
    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

fun Fragment.resetOrientation() {
    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
}

fun Fragment.hideKeyboard() = view?.let { activity?.hideKeyboard(it) }

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.getStandardStringPrefs(key: String, defValue: String) =
    activity?.getStandardStringPrefs(key, defValue)

fun Activity.getStandardStringPrefs(key: String, defValue: String) =
    getStandardPrefs()?.getString(key, defValue)

fun Fragment.setStandardStringPrefs(key: String, value: String) =
    activity?.setStandardStringPrefs(key, value)

fun Activity.setStandardStringPrefs(key: String, value: String) =
    with(getStandardPrefs()?.edit()) {
        this?.putString(key, value)
        this?.apply()
    }

fun Activity.getStandardPrefs(): SharedPreferences? =
    getSharedPreferences(applicationContext?.packageName + "_preferences", Context.MODE_PRIVATE)

fun Array<Array<GameData.GameCell>>.print() {
    val ansiGreen = "\u001B[32m"
    val ansiRed = "\u001B[31m"
    val ansiResetColor = "\u001B[0m"
    println("Current field: ")
    this.forEach { row ->
        row.forEach { cell ->
            cell.apply {
                when (state) {
                    GameData.GameCellState.CROSS -> {
                        print("$ansiRed$state $ansiResetColor")
                    }
                    GameData.GameCellState.CIRCLE -> {
                        print("$ansiGreen$state $ansiResetColor")
                    }
                    else -> {
                        print("$state ")
                    }
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

fun <T> Array<T>.containsStartAt(other: Array<T>): Int? {
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