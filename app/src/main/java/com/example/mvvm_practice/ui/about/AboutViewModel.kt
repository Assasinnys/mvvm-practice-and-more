package com.example.mvvm_practice.ui.about

import androidx.lifecycle.ViewModel

class AboutViewModel : ViewModel() {

    fun getText() =
        "Негласное правило: \"в прилаге зачастую и инет и бд. и получается RemoteUser (инет), User (ui), LocalUser (DB)\"\n" +
                "This app was created and under active development by Arseni (Belarussianin) from Minsk."
}