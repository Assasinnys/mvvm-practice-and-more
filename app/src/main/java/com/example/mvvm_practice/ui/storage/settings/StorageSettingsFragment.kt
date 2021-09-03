package com.example.mvvm_practice.ui.storage.settings

import android.content.Context
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val toolbar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.subtitle = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbms = preferenceManager.findPreference<ListPreference>("dbms_preference_fragment")

        dbms?.setOnPreferenceChangeListener { preference, newValue ->
            Log.i("MYAPP", "NEW STORAGE PREFERENCE: $newValue")
            viewModel.updateDbmsPreferenceById(newValue.toString().toInt())
            true
        }
    }
}

