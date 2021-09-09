package com.example.mvvm_practice.data.room

import androidx.room.*
import com.example.mvvm_practice.data.COLUMN_NAME_ID
import com.example.mvvm_practice.data.LocalUser
import com.example.mvvm_practice.data.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalUserDao {

    @Query("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_NAME_ID ASC")
    fun getLocalUsersASC(): Flow<List<LocalUser>>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_NAME_ID DESC")
    fun getLocalUsersDESC(): Flow<List<LocalUser>>

    @Update
    suspend fun updateLocalUser(user: LocalUser)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: LocalUser)

    @Delete
    suspend fun delete(user: LocalUser)

    @Query("DELETE FROM $TABLE_NAME WHERE $COLUMN_NAME_ID = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()
}