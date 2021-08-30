package com.example.mvvm_practice.ui.storage.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.mvvm_practice.MainViewModel
import com.example.mvvm_practice.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class StorageSettingsFragment : PreferenceFragmentCompat() {

    // The Main View Model
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.storage_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbms = preferenceManager.findPreference<ListPreference>("dbms")

        dbms?.setOnPreferenceChangeListener { _, newValue ->
            Log.i("MYAPP", "NEW STORAGE PREFERENCE: $newValue")
            viewModel.updateDbmsPreferenceById(newValue.toString().toInt())
            true
        }
    }
}

