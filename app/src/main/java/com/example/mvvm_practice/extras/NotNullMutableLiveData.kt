package com.example.mvvm_practice.extras

import androidx.lifecycle.LiveData

class NotNullMutableLiveData<T>(value: T) : LiveData<T>(value) {
    /*
    Wrapping LiveData to provide null safety in Kotlin
     */
    override fun getValue(): T = super.getValue() ?: throw Exception()
    public override fun setValue(value: T) = super.setValue(value)
    public override fun postValue(value: T) = super.postValue(value)
}