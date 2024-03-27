package com.example.yp_playlist_maker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yp_playlist_maker.databinding.ActivitySettingsBinding
import com.example.yp_playlist_maker.settings.view_model.SettingsViewModel
import org.koin.android.ext.android.inject

class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by inject()
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBackFromSettings.setOnClickListener{
            finish()
        }

        binding.switchDarkTheme.isChecked = viewModel.getSavedTheme()
        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
             viewModel.setTheme(isChecked)
        }

        binding.textViewShare.setOnClickListener {
            viewModel.shareApp()
        }

        binding.textViewSendToSupport.setOnClickListener {
            viewModel.openSupport()
        }

        binding.textViewUserAgreement.setOnClickListener {
            viewModel.openTerms()
        }
    }
}