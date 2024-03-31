package com.example.yp_playlist_maker.di

import com.example.yp_playlist_maker.player.domain.PlayerInteractor
import com.example.yp_playlist_maker.player.domain.PlayerInteractorImpl
import com.example.yp_playlist_maker.search.domain.TracksHistoryInteractor
import com.example.yp_playlist_maker.search.domain.TracksHistoryInteractorImpl
import com.example.yp_playlist_maker.search.domain.TracksInteractor
import com.example.yp_playlist_maker.search.domain.TracksInteractorImpl
import com.example.yp_playlist_maker.settings.domain.SettingsInteractor
import com.example.yp_playlist_maker.settings.domain.SettingsInteractorImpl
import com.example.yp_playlist_maker.sharing.domain.SharingInteractor
import com.example.yp_playlist_maker.sharing.domain.SharingInteractorImpl
import org.koin.dsl.module


val domainModule = module {
    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<TracksHistoryInteractor>{
        TracksHistoryInteractorImpl(get())
    }

    factory<SettingsInteractor>{
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor>{
        SharingInteractorImpl(get())
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
}