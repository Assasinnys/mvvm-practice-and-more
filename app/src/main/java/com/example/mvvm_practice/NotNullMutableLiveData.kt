package com.example.mvvm_practice

import androidx.lifecycle.LiveData

@Suppress("UNCHECKED_CAST")
class NotNullMutableLiveData<T>(value: T) : LiveData<T>(value) {
    /*
    Wrapping LiveData to provide null safety in Kotlin
     */
    override fun getValue(): T = super.getValue() as T
    public override fun setValue(value: T) = super.setValue(value)
    public override fun postValue(value: T) = super.postValue(value)
}