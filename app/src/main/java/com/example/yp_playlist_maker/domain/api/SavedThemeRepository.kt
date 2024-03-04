package com.example.yp_playlist_maker.domain.api

interface SavedThemeRepository {
    fun storeDarkTheme(isDarkTheme: Boolean)
    fun retrieveIsDarkTheme(): Boolean
}