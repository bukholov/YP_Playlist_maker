package com.example.yp_playlist_maker.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.domain.db.PlaylistInteractor
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {
    private val stateLiveData: MutableLiveData<Result<List<Playlist>>> by KoinJavaComponent.inject(
        MutableLiveData::class.java
    )

    fun observeState(): LiveData<Result<List<Playlist>>> = stateLiveData

    private fun renderState(state: Result<List<Playlist>>) {
        stateLiveData.postValue(state)
    }

    fun fillData(){
        viewModelScope.launch {
            playlistInteractor
                .playlists()
                .collect{playlists ->
                    renderState(Result.success(playlists))
                }
        }
    }
}