package com.example.yp_playlist_maker.domain.api

interface SavedThemeInteractor {
    fun storeDarkTheme(isDarkTheme: Boolean)
    fun retrieveIsDarkTheme(): Boolean
}