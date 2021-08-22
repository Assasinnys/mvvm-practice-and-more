package com.example.mvvm_practice.repository.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalUserDao {

    @Query("SELECT * FROM local_user_table ORDER BY user_id ASC")
    fun getLocalUsersASC(): Flow<List<LocalUser>>

    @Query("SELECT * FROM local_user_table ORDER BY user_id DESC")
    fun getLocalUsersDESC(): Flow<List<LocalUser>>

    @Update
    suspend fun updateLocalUser(user: LocalUser)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: LocalUser)

    @Delete
    suspend fun delete(user: LocalUser)

    @Query("DELETE FROM local_user_table WHERE user_id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM local_user_table")
    suspend fun deleteAll()
}