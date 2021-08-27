/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mvvm_practice.data

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData

private const val USER_PREFERENCES_NAME = "storage_preferences"
private const val SORT_ORDER_KEY = "order"

enum class SortOrder {
    ID,
    NICKNAME,
    FIRST_NAME,
    SECOND_NAME,
    AGE
}

/**
 * Class that handles saving and retrieving user preferences
 */
class StoragePreferencesRepository private constructor(context: Context) {

    private val sharedPreferences =
        context.applicationContext.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)

    /**
     * Get the sort order. By default, sort order is ID.
     */
    private val _sortOrder = MutableLiveData(getSortOrder())
    val sortOrder = _sortOrder

    private fun getSortOrder() = SortOrder.valueOf(
        sharedPreferences.getString(SORT_ORDER_KEY, SortOrder.ID.name) ?: SortOrder.ID.name
    )

    private fun setSortOrder(newSortOrder: SortOrder) {
        sharedPreferences.edit { putString(SORT_ORDER_KEY, newSortOrder.name) }
    }

    fun updateSortState(newSortOrderId: Int) {
        val newSortOrder = SortOrder.values()[newSortOrderId]
        setSortOrder(newSortOrder)
        _sortOrder.value = newSortOrder
    }

    companion object {
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
