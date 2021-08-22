package com.example.mvvm_practice.ui.storage.about

import androidx.lifecycle.ViewModel
import com.example.mvvm_practice.repository.Repository

class AboutStorageViewModel(private val repository: Repository) : ViewModel() {

    fun getTextAboutStorage() = repository.getTextAboutStorage()
}