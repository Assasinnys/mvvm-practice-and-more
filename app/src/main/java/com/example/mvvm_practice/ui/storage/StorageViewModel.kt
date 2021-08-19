package com.example.mvvm_practice.ui.storage

import android.util.Log
import androidx.lifecycle.*
import com.example.mvvm_practice.extra.NotNullMutableLiveData
import com.example.mvvm_practice.extra.TAG
import com.example.mvvm_practice.ui.storage.model.LocalUser
import com.example.mvvm_practice.ui.storage.model.LocalUserRepository
import kotlinx.coroutines.launch

class StorageViewModel(private val repository: LocalUserRepository) : ViewModel() {

    // Using LiveData and caching what allLocalUsers returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allLocalUsers: LiveData<List<LocalUser>> = repository.allLocalUsers.asLiveData()

    val orderBy: LiveData<Int> = repository.orderBy

    fun updateOrderBy(orderId: Int) {
        repository.orderBy.value = orderId
        Log.i(TAG, "updateOrderBy: ${repository.orderBy.value}")
    }

    fun sortedList(list: List<LocalUser>): List<LocalUser> = when (orderBy.value) {
        0 -> {
            Log.i(TAG, "sortedList: 0, _orderBy.value: ${orderBy.value}")
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

//class StorageViewModelFactory(private val repository: LocalUserRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(StorageViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return StorageViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}