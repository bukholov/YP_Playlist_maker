package com.example.yp_playlist_maker.settings.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {


    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel = ViewModelProvider(this,  SettingsViewModel.getViewModelFactory(this))[SettingsViewModel::class.java]

        val buttonBackFromSettings = findViewById<TextView>(R.id.button_back_from_settings)
        val switchDarkTheme = findViewById<SwitchCompat>(R.id.switch_dark_theme)
        val textViewShare = findViewById<TextView>(R.id.text_view_share)
        val textViewSendToSupport = findViewById<TextView>(R.id.text_view_send_to_support)
        val textViewUserAgreement = findViewById<TextView>(R.id.text_view_user_agreement)

        buttonBackFromSettings.setOnClickListener{
            finish()
        }

        switchDarkTheme.isChecked = viewModel.getSavedTheme()
        switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
             viewModel.setTheme(isChecked)
        }

        textViewShare.setOnClickListener {
            viewModel.shareApp()
        }

        textViewSendToSupport.setOnClickListener {
            viewModel.openSupport()
        }

        textViewUserAgreement.setOnClickListener {
            viewModel.openTerms()
        }
    }
}