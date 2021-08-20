package com.example.mvvm_practice.ui.storage.about

import androidx.lifecycle.ViewModel
import com.example.mvvm_practice.data.LocalUserRepository

class AboutStorageViewModel(private val repository: LocalUserRepository) : ViewModel() {

    fun getTextAboutStorage() = repository.getTextAboutStorage()
}