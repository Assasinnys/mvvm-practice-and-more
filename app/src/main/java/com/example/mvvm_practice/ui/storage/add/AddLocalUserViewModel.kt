package com.example.mvvm_practice.ui.storage.add

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm_practice.data.Repository
import com.example.mvvm_practice.data.room.LocalUser
import kotlinx.coroutines.launch

class AddLocalUserViewModel(private val repository: Repository) : ViewModel() {

    val ageAutoCompleteTextViewItems = Array(106) { index -> index + 5 }

    fun addUser(
        nickname: Editable?,
        firstName: Editable?,
        secondName: Editable?,
        age: Editable?,
        id: Int? = null
    ): Boolean {
        if (nickname.toString().contentEquals("")) return false
        if (id == null) {
            insert(
                LocalUser(
                    nickname = nickname.toString(),
                    firstName = firstName.toString(),
                    secondName = secondName.toString(),
                    age = age.toString().toIntOrNull()
                )
            )
        } else {
            update(
                LocalUser(
                    id = id,
                    nickname = nickname.toString(),
                    firstName = firstName.toString(),
                    secondName = secondName.toString(),
                    age = age.toString().toIntOrNull()
                )
            )
        }
        return true
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    private fun insert(localUser: LocalUser) = viewModelScope.launch {
        repository.insert(localUser)
    }

    private fun update(localUser: LocalUser) = viewModelScope.launch {
        repository.update(localUser)
    }
}