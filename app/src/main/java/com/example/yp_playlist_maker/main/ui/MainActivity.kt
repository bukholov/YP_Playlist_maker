package com.example.yp_playlist_maker.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yp_playlist_maker.databinding.ActivityMainBinding
import com.example.yp_playlist_maker.media.ui.MediaActivity
import com.example.yp_playlist_maker.search.ui.SearchActivity
import com.example.yp_playlist_maker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSearch.setOnClickListener{startActivity(Intent(this, SearchActivity::class.java)) }

        binding.buttonMedia.setOnClickListener { startActivity(Intent(this, MediaActivity::class.java))}

        binding.buttonSettings.setOnClickListener{startActivity(Intent(this, SettingsActivity::class.java)) }
    }
}