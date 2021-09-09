package com.example.mvvm_practice.extras

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.mvvm_practice.gameCore.GameData

fun Fragment.hideKeyboard() = view?.let { activity?.hideKeyboard(it) }

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

inline fun <reified T : Any> Fragment.getFromStandardPrefs(key: String, defValue: T) =
    activity?.getFromStandardPrefs(key, defValue) ?: defValue

inline fun <reified T> Fragment.putIntoStandardPrefs(key: String, value: T) {
    activity?.putIntoStandardPrefs(key, value)
}

inline fun <reified T : Any> Activity.getFromStandardPrefs(key: String, defValue: T) =
    when (T::class) {
        String::class -> {
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
                ?.getString(key, defValue as String) ?: defValue
        }
        else -> defValue
    }

inline fun <reified T> Activity.putIntoStandardPrefs(key: String, value: T) {
    PreferenceManager.getDefaultSharedPreferences(applicationContext)?.edit()?.apply {
        when (T::class) {
            String::class -> putString(key, value as String)
        }
        apply()
    }
}

fun Cursor.getStringValue(columnName: String): String? {
    val columnIndex: Int
    try {
        columnIndex = getColumnIndexOrThrow(columnName)
    } catch (ex: IllegalArgumentException) {
        Log.e(TAG, "getColumnValue: ${ex.message + " " + ex.cause}}")
        return null
    }
    return getStringOrNull(columnIndex)
}

fun Cursor.getIntValue(columnName: String): Int? {
    val columnIndex: Int
    try {
        columnIndex = getColumnIndexOrThrow(columnName)
    } catch (ex: IllegalArgumentException) {
        Log.e(TAG, "getColumnValue: ${ex.message + " " + ex.cause}}")
        return null
    }
    return getIntOrNull(columnIndex)
}

fun Array<Array<GameData.GameCell>>.print() {
    //val ansiGreen = "\u001B[32m"
    //val ansiRed = "\u001B[31m"
    //val ansiResetColor = "\u001B[0m"
    println("Current field: ")
    this.forEach { row ->
        row.forEach { cell ->
            cell.apply {
                when (state) {
                    GameData.GameCellState.CROSS -> {
                        Log.i(TAG, "$state ")
                    }
                    GameData.GameCellState.CIRCLE -> {
                        Log.i(TAG, "$state ")
                    }
                    else -> {
                        Log.i(TAG, "$state ")
                    }
                }
            }
        }
        Log.i(TAG, "...")
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