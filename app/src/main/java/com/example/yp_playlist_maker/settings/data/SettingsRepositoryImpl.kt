package com.example.yp_playlist_maker.settings.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.yp_playlist_maker.search.repository.THEME_PREFERENCES
import com.example.yp_playlist_maker.search.repository.THEME_TEXT
import com.example.yp_playlist_maker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(val context: Context): SettingsRepository {
    private val sharedPreferences = context.getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
    override fun storeDarkTheme(isDarkTheme: Boolean){
        sharedPreferences.edit().putBoolean(THEME_TEXT, isDarkTheme).apply()
    }

    override fun retrieveIsDarkTheme(): Boolean{
        return sharedPreferences.getBoolean(THEME_TEXT, false)
    }
}