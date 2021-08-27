package com.example.mvvm_practice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mvvm_practice.databinding.MainActivityBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    // The View Binding
    private lateinit var binding: MainActivityBinding

    // The View Model
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater).apply {
            setContentView(root)
            setSupportActionBar(appContentMain.appBarMain.toolbar)

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            setupActionBarWithNavController(
                navController, AppBarConfiguration(
                    setOf(
                        R.id.nav_game, R.id.nav_storage, R.id.nav_settings, R.id.nav_about
                    ), drawerLayout
                )
            )
            navView.setupWithNavController(navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(
            AppBarConfiguration(
                setOf(
                    R.id.nav_game, R.id.nav_storage, R.id.nav_settings, R.id.nav_about
                ), binding.drawerLayout
            )
        ) || super.onSupportNavigateUp()
    }
}