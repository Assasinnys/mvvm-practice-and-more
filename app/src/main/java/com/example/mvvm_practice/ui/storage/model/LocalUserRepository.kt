package com.example.mvvm_practice.ui.storage.model

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.mvvm_practice.extra.NotNullMutableLiveData
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class LocalUserRepository(private val localUserDao: LocalUserDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allLocalUsers: Flow<List<LocalUser>> = localUserDao.getLocalUsersASC()

    val orderBy: NotNullMutableLiveData<Int> = NotNullMutableLiveData(0)
    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(localUser: LocalUser) {
        localUserDao.insert(localUser)
    }
}