package com.example.yp_playlist_maker.settings.domain

interface SettingsInteractor {
    fun storeDarkTheme(isDarkTheme: Boolean)
    fun retrieveIsDarkTheme(): Boolean
}