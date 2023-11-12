package com.example.yp_playlist_maker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBackFromSettings = findViewById<TextView>(R.id.buttonBackFromSettings)
        buttonBackFromSettings.setOnClickListener{
            finish()
        }
    }
}