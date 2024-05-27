package com.example.yp_playlist_maker.di

import com.example.yp_playlist_maker.media.viewmodel.FavoriteTracksViewModel
import com.example.yp_playlist_maker.media.viewmodel.PlaylistsViewModel
import com.example.yp_playlist_maker.player.ui.view_model.AudioPlayerViewModel
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
        AudioPlayerViewModel(get(), get(), get())
    }

    viewModel<PlaylistsViewModel>{
        PlaylistsViewModel()
    }

    viewModel<FavoriteTracksViewModel>{
        FavoriteTracksViewModel(get())
    }
}