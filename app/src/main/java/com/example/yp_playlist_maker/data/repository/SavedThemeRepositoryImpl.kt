package com.example.yp_playlist_maker.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.yp_playlist_maker.domain.api.SavedThemeRepository

class SavedThemeRepositoryImpl(val context: Context):SavedThemeRepository {
    private val sharedPreferences =context.getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
    override fun storeDarkTheme(isDarkTheme: Boolean){
        sharedPreferences.edit().putBoolean(THEME_TEXT, isDarkTheme).apply()
    }

    override fun retrieveIsDarkTheme(): Boolean{
        return sharedPreferences.getBoolean(THEME_TEXT, false)
    }
}