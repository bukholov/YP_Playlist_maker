package com.example.yp_playlist_maker.main.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.creator.Creator
import com.example.yp_playlist_maker.media.ui.MediaActivity
import com.example.yp_playlist_maker.search.ui.SearchActivity
import com.example.yp_playlist_maker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        buttonSearch.setOnClickListener{startActivity(Intent(this, SearchActivity::class.java)) }

        val buttonMedia = findViewById<Button>(R.id.button_media)
        buttonMedia.setOnClickListener { startActivity(Intent(this, MediaActivity::class.java))}

        val buttonSettings = findViewById<Button>(R.id.button_settings)
        buttonSettings.setOnClickListener{startActivity(Intent(this, SettingsActivity::class.java)) }

        val themeIsDark =  Creator.provideSettingsInteractor(applicationContext).retrieveIsDarkTheme()

        if (themeIsDark) { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
        else { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
    }


}