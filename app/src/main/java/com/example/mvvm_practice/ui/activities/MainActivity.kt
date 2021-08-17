package com.example.mvvm_practice.ui.activities

import com.example.mvvm_practice.game.GameData.Companion.indexIntoPosition
import com.example.mvvm_practice.game.GameData.GameCellState
import com.example.mvvm_practice.game.GameData.GameState
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatImageButton
import com.example.mvvm_practice.Grid
import com.example.mvvm_practice.R
import com.example.mvvm_practice.TAG
import com.example.mvvm_practice.viewModel.MainViewModel
import com.example.mvvm_practice.databinding.MainActivityBinding
import com.example.mvvm_practice.game.GameData.Companion.gameModeToInt
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    // The View Binding
    private lateinit var binding: MainActivityBinding

    // The View Model
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }
}