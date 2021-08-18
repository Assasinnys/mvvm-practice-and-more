package com.example.mvvm_practice.ui.storage.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.mvvm_practice.R

class StorageSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.storage_preferences, rootKey)
    }
}