package com.example.mvvm_practice.ui.about

import androidx.lifecycle.ViewModel
import com.example.mvvm_practice.data.LocalUserRepository

class AboutViewModel(private val repository: LocalUserRepository) : ViewModel() {

    fun getTextAboutApp() = repository.getTextAboutApp()
}