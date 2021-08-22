package com.example.mvvm_practice.repository

import com.example.mvvm_practice.repository.room.LocalUser
import com.example.mvvm_practice.repository.room.LocalUserDao
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class Repository(private val localUserDao: LocalUserDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allLocalUsers: Flow<List<LocalUser>> = localUserDao.getLocalUsersASC()

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
    //@WorkerThread
    suspend fun insert(localUser: LocalUser) {
        localUserDao.insert(localUser)
    }

    //@Suppress("RedundantSuspendModifier")
    //@WorkerThread
    suspend fun update(localUser: LocalUser) {
        localUserDao.updateLocalUser(localUser)
    }

    //@Suppress("RedundantSuspendModifier")
    //@WorkerThread
    suspend fun deleteById(id: Int) {
        localUserDao.deleteById(id)
    }

    //@Suppress("RedundantSuspendModifier")
    //@WorkerThread
    suspend fun delete(localUser: LocalUser) {
        localUserDao.delete(localUser)
    }
}