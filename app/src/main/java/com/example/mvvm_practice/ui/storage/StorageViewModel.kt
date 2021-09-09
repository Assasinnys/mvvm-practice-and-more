package com.example.mvvm_practice.ui.storage

import android.util.Log
import androidx.lifecycle.*
import com.example.mvvm_practice.data.LocalUser
import com.example.mvvm_practice.data.Repository
import com.example.mvvm_practice.data.StoragePreferencesRepository.Companion.DBMS
import com.example.mvvm_practice.data.StoragePreferencesRepository.Companion.SortOrder
import com.example.mvvm_practice.extras.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class StorageViewModel(
    private val repository: Repository
) : ViewModel() {

    val storageSortOrder: LiveData<SortOrder> =
        repository.storagePreferencesRepository.sortOrder.distinctUntilChanged()
    val storageDBMS: LiveData<DBMS> =
        repository.storagePreferencesRepository.dbms.distinctUntilChanged()

    val allLocalUsers: LiveData<List<LocalUser>> =
        repository.allLocalUsers.asLiveData().distinctUntilChanged()

    fun updateSortStateById(newSortOrderId: Int) {
        repository.storagePreferencesRepository.updateSort(newSortOrderId)
    }

    fun getOrderedAllLocalUsers(
        idOfSort: Int? = null,
        allLocalUserList: List<LocalUser>? = null
    ): List<LocalUser> {
        (allLocalUserList ?: allLocalUsers.value)?.let { list ->
            return when (idOfSort ?: storageSortOrder.value?.ordinal ?: 0) {
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