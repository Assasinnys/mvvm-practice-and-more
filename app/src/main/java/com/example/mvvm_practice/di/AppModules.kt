package com.example.mvvm_practice.di

import com.example.mvvm_practice.ui.storage.StorageViewModel
import com.example.mvvm_practice.ui.storage.add.AddLocalUserViewModel
import com.example.mvvm_practice.repository.Repository
import com.example.mvvm_practice.repository.room.LocalUserDatabase
import com.example.mvvm_practice.ui.about.AboutViewModel
import com.example.mvvm_practice.ui.storage.about.AboutStorageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { CoroutineScope(SupervisorJob()) }

    // Database
    single { LocalUserDatabase.getDatabase(get(), get()) }
    // Repository
    single { Repository(get<LocalUserDatabase>().localUserDao()) }

    viewModel { StorageViewModel(get()) }
    viewModel { AddLocalUserViewModel(get()) }
    viewModel { AboutViewModel(get()) }
    viewModel { AboutStorageViewModel(get()) }
}