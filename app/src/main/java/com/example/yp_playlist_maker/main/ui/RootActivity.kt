package com.example.yp_playlist_maker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.ActivityRootBinding
import com.example.yp_playlist_maker.media.ui.MediaFragment
import com.example.yp_playlist_maker.search.ui.SearchFragment
import com.example.yp_playlist_maker.settings.ui.SettingsFragment

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.search_fragment-> {
                    supportFragmentManager
                        .commit {
                            replace(R.id.container_view, SearchFragment())
                        }
                    true
                }
                R.id.media_fragment -> {
                    supportFragmentManager
                        .commit {
                            replace(R.id.container_view, MediaFragment())
                        }
                    true
                }
                R.id.settings_fragment ->{
                    supportFragmentManager
                        .commit {
                            replace(R.id.container_view, SettingsFragment())
                        }
                    true
                }
                else -> {
                    true
                }
            }
        }
        bottomNavigationView.selectedItemId = R.id.media_fragment
    }
}