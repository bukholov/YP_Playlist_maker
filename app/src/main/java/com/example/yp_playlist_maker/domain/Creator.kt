package com.example.yp_playlist_maker.domain

import android.content.Context
import com.example.yp_playlist_maker.data.network.RetrofitNetworkClient
import com.example.yp_playlist_maker.data.repository.SavedThemeRepositoryImpl
import com.example.yp_playlist_maker.data.repository.TracksHistoryRepositoryImpl
import com.example.yp_playlist_maker.data.repository.TracksRepositoryImpl
import com.example.yp_playlist_maker.domain.api.SavedThemeInteractor
import com.example.yp_playlist_maker.domain.api.SavedThemeRepository
import com.example.yp_playlist_maker.domain.api.TracksHistoryInteractor
import com.example.yp_playlist_maker.domain.api.TracksHistoryRepository
import com.example.yp_playlist_maker.domain.api.TracksInteractor
import com.example.yp_playlist_maker.domain.api.TracksRepository
import com.example.yp_playlist_maker.domain.impl.SavedThemeInteractorImpl
import com.example.yp_playlist_maker.domain.impl.TracksHistoryInteractorImpl
import com.example.yp_playlist_maker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getTracksHistoryRepository(context: Context): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl(context = context)
    }

    fun provideTracksHistoryInteractor(context: Context): TracksHistoryInteractor {
        return TracksHistoryInteractorImpl(getTracksHistoryRepository(context))
    }

    private fun getSavedThemeRepository(context: Context): SavedThemeRepository {
        return SavedThemeRepositoryImpl(context = context)
    }

    fun provideSavedThemeInteractor(context: Context): SavedThemeInteractor {
        return SavedThemeInteractorImpl(getSavedThemeRepository(context))
    }
}