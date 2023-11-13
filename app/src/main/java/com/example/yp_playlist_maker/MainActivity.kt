package com.example.yp_playlist_maker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.buttonSearch)
        buttonSearch.setOnClickListener{startActivity(Intent(this, SearchActivity::class.java)) }

        val buttonMedia = findViewById<Button>(R.id.buttonMedia)
        buttonMedia.setOnClickListener { startActivity(Intent(this, MediaActivity::class.java))}

        val buttonSettings = findViewById<Button>(R.id.buttonSettings)
        buttonSettings.setOnClickListener{startActivity(Intent(this, SettingsActivity::class.java)) }
    }


}