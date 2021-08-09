package com.example.mvvm_practice.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.mvvm_practice.model.GameInfo

class MainViewModel : ViewModel() {
    var gameInfo = liveData {
        emit(GameInfo.getStandardGameInfo())
    }
}