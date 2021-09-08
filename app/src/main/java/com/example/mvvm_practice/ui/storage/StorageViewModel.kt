package com.example.mvvm_practice.ui.storage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvm_practice.data.LocalUser
import com.example.mvvm_practice.data.Repository
import com.example.mvvm_practice.data.StoragePreferencesRepository
import com.example.mvvm_practice.data.StoragePreferencesRepository.Companion.DBMS
import com.example.mvvm_practice.data.StoragePreferencesRepository.Companion.SortOrder
import com.example.mvvm_practice.extras.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class StorageViewModel(
    private val repository: Repository,
    private val storagePreferencesRepository: StoragePreferencesRepository
) : ViewModel() {

    // Using LiveData and caching what allLocalUsers returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allLocalUsers: LiveData<List<LocalUser>> = repository.allLocalUsers.asLiveData()

    /**
     * Observable storage preferences
     */
    val storageSortOrder: LiveData<SortOrder> = storagePreferencesRepository.sortOrder
    val storageDBMS: LiveData<DBMS> = storagePreferencesRepository.dbms

    fun updateSortStateById(newSortOrderId: Int) {
        storagePreferencesRepository.updateSort(newSortOrderId)
    }

    fun updateDbmsPreferenceById(newDbmsId: Int) {
        storagePreferencesRepository.updateDbms(newDbmsId)
    }

    fun getOrderedAllLocalUsers(idOfSort: Int): List<LocalUser> {
        allLocalUsers.value?.let { list ->
            return when (idOfSort) {
                0 -> {
                    Log.i(TAG, "sortedList: 0")
                    list.sortedBy { it.id }
                }
                1 -> {
                    Log.i(TAG, "sortedList: 1")
                    list.sortedBy { it.nickname }
                }
                2 -> {
                    Log.i(TAG, "sortedList: 2")
                    list.sortedBy { it.firstName }
                }
                3 -> {
                    Log.i(TAG, "sortedList: 3")
                    list.sortedBy { it.secondName }
                }
                4 -> {
                    Log.i(TAG, "sortedList: 4")
                    list.sortedBy { it.age }
                }
                else -> list
            }
        }
        return emptyList()
    }

    fun deleteById(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }

    fun delete(localUser: LocalUser) = viewModelScope.launch {
        repository.delete(localUser)
    }

    private fun <T> Flow<T>.asLiveDataFlow() =
        shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)
}