package com.example.yp_playlist_maker.di

import com.example.yp_playlist_maker.player.view_model.AudioPlayerViewModel
import com.example.yp_playlist_maker.search.view_model.SearchViewModel
import com.example.yp_playlist_maker.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<SearchViewModel>{
        SearchViewModel()
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get())
    }

    viewModel<AudioPlayerViewModel> {
        AudioPlayerViewModel(get(), get())
    }
}