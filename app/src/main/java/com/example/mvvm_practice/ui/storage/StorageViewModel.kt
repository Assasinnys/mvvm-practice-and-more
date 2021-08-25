package com.example.mvvm_practice.ui.storage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm_practice.extras.TAG
import com.example.mvvm_practice.repository.Repository
import com.example.mvvm_practice.repository.room.LocalUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//class UsersViewModel : ViewModel() {
//
//    private val repository: Repository by locateLazy()
//    private val preferences: Preferences by locateLazy()
//
//    val users: Flow<List<User>> = preferences.orderFlow.flatMapLatest { repository.getOrderedAllLocalUsers(it) }
//}
//
//class Preferences(
//    private val context: Context
//) {
//
//    private val sharedPreferences by lazy { context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE) }
//
//    val orderFlow: Flow<Int> = sharedPreferences.getIntFlow(ORDER_KEY, 0)
//
//    var order: Int
//        get() = sharedPreferences.getInt(ORDER_KEY, DEFAULT_ORDER)
//        set(value) = sharedPreferences.edit { putInt(ORDER_KEY, value) }
//
//
//    companion object {
//        private const val PREFERENCES_NAME = "app_preferences"
//        private const val ORDER_KEY = "order"
//        private const val DEFAULT_ORDER = 0
//    }
//}
//
//@ExperimentalCoroutinesApi
//fun SharedPreferences.getIntFlow(key: String, defaultValue: Int = 0) = callbackFlow<Int> {
//
//    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key -> offer(getInt(key, defaultValue)) }
//
//    registerOnSharedPreferenceChangeListener(listener)
//
//    runCatching { getInt(key, defaultValue) }
//
//    awaitClose {
//        unregisterOnSharedPreferenceChangeListener(listener)
//    }
//}

class StorageViewModel(private val repository: Repository) : ViewModel() {

    // Using LiveData and caching what allLocalUsers returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allLocalUsers: Flow<List<LocalUser>> = repository.allLocalUsers.asLiveDataFlow()

    fun getOrderedAllLocalUsers(idOfSort: Int): List<LocalUser> {
        var currentList: List<LocalUser>
        runBlocking {
            currentList = allLocalUsers.first()
        }
        currentList.let { list ->
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
    }

    /**
     * Launching a new coroutine to manage the data in a non-blocking way
     */
    fun deleteById(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }

    fun delete(localUser: LocalUser) = viewModelScope.launch {
        repository.delete(localUser)
    }

    fun update(localUser: LocalUser) = viewModelScope.launch {
        repository.update(localUser)
    }

    private fun <T> Flow<T>.asLiveDataFlow() =
        shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)
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