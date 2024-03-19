package com.example.yp_playlist_maker.settings.domain

interface SettingsRepository {
    fun storeDarkTheme(isDarkTheme: Boolean)
    fun retrieveIsDarkTheme(): Boolean
}