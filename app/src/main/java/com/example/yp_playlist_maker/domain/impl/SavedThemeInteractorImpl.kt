package com.example.yp_playlist_maker.domain.impl

import com.example.yp_playlist_maker.domain.api.SavedThemeInteractor
import com.example.yp_playlist_maker.domain.api.SavedThemeRepository

class SavedThemeInteractorImpl(private val repository: SavedThemeRepository):SavedThemeInteractor {

    override fun storeDarkTheme(isDarkTheme: Boolean){
        repository.storeDarkTheme(isDarkTheme = isDarkTheme)
    }

    override fun retrieveIsDarkTheme(): Boolean{
        return repository.retrieveIsDarkTheme()
    }
}