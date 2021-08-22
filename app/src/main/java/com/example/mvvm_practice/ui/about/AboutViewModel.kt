package com.example.mvvm_practice.ui.about

import androidx.lifecycle.ViewModel
import com.example.mvvm_practice.repository.Repository

class AboutViewModel(private val repository: Repository) : ViewModel() {

    fun getTextAboutApp() = repository.getTextAboutApp()
}