package com.example.mvvm_practice.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_practice.game.Game

class MainViewModel : ViewModel() {
    val game: LiveData<Game> = object : LiveData<Game>(Game()){}

    init {
        Log.i("ViewModel", "MainViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("ViewModel", "MainViewModel destroyed!")
    }
}