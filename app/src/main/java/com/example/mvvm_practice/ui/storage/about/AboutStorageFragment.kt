package com.example.mvvm_practice.ui.storage.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.fragment.app.Fragment
import com.example.mvvm_practice.R
import com.example.mvvm_practice.databinding.FragmentAboutStorageBinding
import com.google.android.material.composethemeadapter.MdcTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutStorageFragment : Fragment() {

    // The View Model
    private val viewModel by viewModel<AboutStorageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAboutStorageBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ComposeView>(R.id.about_storage_screen)?.let {
            it.setContent {
                MdcTheme {
                    Screen()
                }
            }
        }
    }

    @Composable
    private fun Screen() {
        MainText(viewModel.getTextAboutStorage())
    }

    @Composable
    fun MainText(text: String) {
        Text(
            text = text,
            Modifier
                .padding(horizontal = dimensionResource(R.dimen.medium_padding))
                .fillMaxSize(),
            style = TextStyle()
        )
    }
}