package com.example.mvvm_practice.ui.storage.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.get
import com.example.mvvm_practice.R
import com.example.mvvm_practice.ui.storage.StorageViewModel
import com.example.mvvm_practice.ui.storage.add.AddLocalUserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.getScopeId
import org.koin.core.component.getScopeName

class StorageSettingsFragment : PreferenceFragmentCompat() {

    // The View Model
    private val viewModel by viewModel<StorageViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.storage_preferences, rootKey)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i("GAME", "onOptionsItemSelected: ${item.itemId}")
        return super.onOptionsItemSelected(item)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {

        return super.onPreferenceTreeClick(preference)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderPreference =
            preferenceManager.findPreference<ListPreference>("order") as ListPreference



        orderPreference.setOnPreferenceChangeListener { preference, newValue ->
            viewModel.updateOrderBy(newValueToOrderId(newValue.toString()))
            Log.i("GAME", "summary changed: \"${preference.summary}\" to \"$newValue\"")
            true
        }
    }

    private fun newValueToOrderId(newValue: String): Int {
        if (newValue == "id") return 0
        if (newValue == "nickname") return 1
        if (newValue == "first_name") return 2
        if (newValue == "second_name") return 3
        if (newValue == "age") return 4
        return 22
    }
}

