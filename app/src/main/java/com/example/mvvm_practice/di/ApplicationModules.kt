package com.example.mvvm_practice.di

import com.example.mvvm_practice.ui.storage.StorageViewModel
import com.example.mvvm_practice.ui.storage.add.AddLocalUserViewModel
import com.example.mvvm_practice.data.LocalUserRepository
import com.example.mvvm_practice.data.LocalUserRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { CoroutineScope(SupervisorJob()) }

    // Database
    single { LocalUserRoomDatabase.getDatabase(get(), get()) }
    // Repository
    single { LocalUserRepository(get<LocalUserRoomDatabase>().localUserDao()) }

    viewModel { StorageViewModel(get()) }
    viewModel { AddLocalUserViewModel(get()) }
}