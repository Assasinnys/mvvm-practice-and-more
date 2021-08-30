package com.example.mvvm_practice

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_practice.data.StoragePreferencesRepository
import com.example.mvvm_practice.data.StoragePreferencesRepository.Companion.DBMS

class MainViewModel(
    private val storagePreferencesRepository: StoragePreferencesRepository
) : ViewModel() {
    /**
     * Observable storage preference
     */
    val storageDBMS: LiveData<DBMS> =
        storagePreferencesRepository.dbms

    fun updateDbmsPreferenceById(newDbmsId: Int) {
        storagePreferencesRepository.updateDbms(newDbmsId)
    }
}