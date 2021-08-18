package com.example.mvvm_practice.ui.storage

import androidx.lifecycle.*
import com.example.mvvm_practice.ui.storage.model.LocalUser
import com.example.mvvm_practice.ui.storage.model.LocalUserRepository
import kotlinx.coroutines.launch

class StorageViewModel(private val repository: LocalUserRepository) : ViewModel() {

    // Using LiveData and caching what allLocalUsers returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allLocalUsers: LiveData<List<LocalUser>> = repository.allLocalUsers.asLiveData()
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