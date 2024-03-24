package com.example.yp_playlist_maker.creator

import android.content.Context
import com.example.yp_playlist_maker.player.domain.PlayerInteractor
import com.example.yp_playlist_maker.player.domain.PlayerInteractorImpl
import com.example.yp_playlist_maker.player.domain.PlayerRepository
import com.example.yp_playlist_maker.player.domain.PlayerRepositoryImpl
import com.example.yp_playlist_maker.search.data.network.RetrofitNetworkClient
import com.example.yp_playlist_maker.search.domain.TracksHistoryInteractor
import com.example.yp_playlist_maker.search.domain.TracksHistoryInteractorImpl
import com.example.yp_playlist_maker.search.domain.TracksHistoryRepository
import com.example.yp_playlist_maker.search.domain.TracksInteractor
import com.example.yp_playlist_maker.search.domain.TracksInteractorImpl
import com.example.yp_playlist_maker.search.domain.TracksRepository
import com.example.yp_playlist_maker.search.repository.TracksHistoryRepositoryImpl
import com.example.yp_playlist_maker.search.repository.TracksRepositoryImpl
import com.example.yp_playlist_maker.settings.data.SettingsRepositoryImpl
import com.example.yp_playlist_maker.settings.domain.SettingsInteractor
import com.example.yp_playlist_maker.settings.domain.SettingsInteractorImpl
import com.example.yp_playlist_maker.settings.domain.SettingsRepository
import com.example.yp_playlist_maker.sharing.data.ExternalNavigator
import com.example.yp_playlist_maker.sharing.data.SharingRepositoryImpl
import com.example.yp_playlist_maker.sharing.domain.SharingInteractor
import com.example.yp_playlist_maker.sharing.domain.SharingInteractorImpl
import com.example.yp_playlist_maker.sharing.domain.SharingRepository

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

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context = context)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getSharingRepository(context: Context): SharingRepository {
        return SharingRepositoryImpl(ExternalNavigator(context), context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor{
        return SharingInteractorImpl(getSharingRepository(context))
    }

    private fun getPlayerRepository(context: Context): PlayerRepository {
        return PlayerRepositoryImpl(context)
    }

    fun providePlayerRepository(context: Context): PlayerInteractor{
        return PlayerInteractorImpl(getPlayerRepository(context))
    }
}