package com.example.mvvm_practice.data

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.mvvm_practice.data.cursor.LocalUserCursorDatabase
import com.example.mvvm_practice.data.room.LocalUserDao
import com.example.mvvm_practice.extras.TAG
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*


// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class Repository(
    private val localUserDaoRoom: LocalUserDao,
    private val localUserCursorDatabase: LocalUserDao,
    private val context: Context
) {
    private val storagePreferencesRepository = StoragePreferencesRepository.getInstance(context)

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    var allLocalUsers: LiveData<List<LocalUser>> = getDao().getLocalUsersASC().asLiveData(Dispatchers.IO)

    private fun getDao(): LocalUserDao {
        return when (storagePreferencesRepository.dbms.value) {
            StoragePreferencesRepository.Companion.DBMS.CURSOR -> {
                Log.i(TAG, "getDao: CURSOR")
                localUserCursorDatabase
            }
            StoragePreferencesRepository.Companion.DBMS.ROOM -> {
                Log.i(TAG, "getDao: ROOM")
                localUserDaoRoom
            }
            else -> localUserCursorDatabase
        }
    }

    private suspend fun notifyListChange() {
//        allLocalUsers = getDao().getLocalUsersASC()
//        var list: List<LocalUser> = emptyList()
//        allLocalUsers.collect {
//            list = it
//        }
        Log.i(TAG, "notifyListChange LIST: ${allLocalUsers.value?.last()}")
    }

    fun getTextAboutApp() =
        "Негласное правило: \"в прилаге зачастую и инет и бд. и получается RemoteUser (инет), User (ui), LocalUser (DB)\"\n" +
                "This app was created and under active development by Arseni (Belarussianin) from Minsk.\n" +
                "Github account: https://github.com/Belarussianin"

    fun getTextAboutStorage() =
        "This is task 4 from rs.school https://github.com/rolling-scopes-school/rs.android.task.4 \n" +
                "My completed task & more: https://github.com/Belarussianin/mvvm-practice-and-more" +
                "\nIn the top app bar buttons: settings, add user.\nSwipe left or right to delete item\nThank you for your attention."

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.

    //@Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(localUser: LocalUser) {
        getDao().insert(localUser)
        notifyListChange()
    }

    @WorkerThread
    suspend fun updateLocalUser(localUser: LocalUser) {
        getDao().updateLocalUser(localUser)
        notifyListChange()
    }

    @WorkerThread
    suspend fun deleteById(id: Int) {
        getDao().deleteById(id)
        notifyListChange()
    }

    @WorkerThread
    suspend fun delete(localUser: LocalUser) {
        getDao().delete(localUser)
        notifyListChange()
    }
}

