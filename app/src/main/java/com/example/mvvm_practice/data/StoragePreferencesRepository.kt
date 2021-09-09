package com.example.mvvm_practice.data

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.mvvm_practice.extras.TAG

/**
 * Class that handles saving and retrieving storage preferences
 */
class StoragePreferencesRepository private constructor(context: Context) {

    private val preferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    private val _sortOrder =
        MutableLiveData(getEnumPreference(getPreference(SORT_ORDER_KEY, SortOrder.ID)))
    private val _dbms = MutableLiveData(getEnumPreference(getPreference(DBMS_KEY, DBMS.ROOM)))

    val sortOrder = _sortOrder
    val dbms = _dbms

    private inline fun <reified T : Enum<T>> getEnumPreference(
        defaultValue: T
    ): T = when (defaultValue) {
        is SortOrder -> getSortOrderByName(getPreference(SORT_ORDER_KEY, SortOrder.ID.name)) as T
        is DBMS -> getDBMSByName(getPreference(DBMS_KEY, DBMS.ROOM.name)) as T
        else -> defaultValue
    }

    fun updateSort(newSortOrderId: Int) {
        if (newSortOrderId in SortOrder.values().indices) {
            val newSortOrder = SortOrder.values()[newSortOrderId]
            setPreference(SORT_ORDER_KEY, newSortOrder.name)
            if (_sortOrder.value != newSortOrder) {
                _sortOrder.value = newSortOrder
            }
        }
    }

    fun updateDbms(newDbmsId: Int) {
        if (newDbmsId in DBMS.values().indices) {
            val newDBMS = DBMS.values()[newDbmsId]
            setPreference(DBMS_KEY, newDBMS.name)
            if (_dbms.value != newDBMS) {
                _dbms.value = newDBMS
            }
        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    private inline fun <reified T : Any> getPreference(
        key: String,
        defaultValue: T
    ): T = when (T::class) {
        String::class -> {
            val test = preferences.getString(key, defaultValue as String) ?: defaultValue
            Log.i(TAG, "getPreference String: $test")
            test
        }
        Int::class -> preferences.getInt(key, defaultValue as Int)
        Boolean::class -> preferences.getBoolean(key, defaultValue as Boolean)
        else -> defaultValue
    } as T

    private fun <T : Any> setPreference(
        key: String,
        value: T
    ) {
        when (value) {
            is String -> preferences.edit { putString(key, value as String) }
            is Int -> preferences.edit { putInt(key, value as Int) }
            is Boolean -> preferences.edit { putBoolean(key, value as Boolean) }
        }
    }

    companion object {
        enum class SortOrder {
            ID,
            NICKNAME,
            FIRST_NAME,
            SECOND_NAME,
            AGE
        }

        enum class DBMS {
            ROOM,
            CURSOR
        }

        fun getSortOrderByName(name: String): SortOrder =
            SortOrder.values().find { it.name == name } ?: SortOrder.ID

        fun getDBMSByName(name: String): DBMS =
            DBMS.values().find { it.name == name } ?: DBMS.ROOM

        //const val PREFERENCES_NAME = "storage_preferences"
        const val SORT_ORDER_KEY = "order"
        const val DBMS_KEY = "dbms"

        @Volatile
        private var INSTANCE: StoragePreferencesRepository? = null

        fun getInstance(context: Context): StoragePreferencesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = StoragePreferencesRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }
}