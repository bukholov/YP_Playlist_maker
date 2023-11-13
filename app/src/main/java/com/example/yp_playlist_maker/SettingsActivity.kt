package com.example.yp_playlist_maker

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBackFromSettings = findViewById<TextView>(R.id.buttonBackFromSettings)
        buttonBackFromSettings.setOnClickListener{
            finish()
        }

        val switchDarkTheme = findViewById<SwitchCompat>(R.id.switchDarkTheme)
        switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
            else { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
        }


        val textViewShare = findViewById<TextView>(R.id.textViewShare)
        textViewShare.setOnClickListener { Intent(Intent.ACTION_SEND) }

        val textViewSendToSupport = findViewById<TextView>(R.id.textViewSendToSupport)
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(getString(R.string.mailto))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.dev_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_email))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.message_email))
        }
        textViewSendToSupport.setOnClickListener { startActivity(emailIntent) }

        val textViewUserAgreement = findViewById<TextView>(R.id.textViewUserAgreement)
        textViewUserAgreement.setOnClickListener {startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.uri_user_agreement)))) }
    }
}