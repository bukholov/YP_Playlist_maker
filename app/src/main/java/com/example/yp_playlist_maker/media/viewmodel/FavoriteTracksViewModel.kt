package com.example.yp_playlist_maker.media.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.player.domain.db.FavoriteTracksRepository
import com.example.yp_playlist_maker.player.ui.AudioPlayerActivity
import com.example.yp_playlist_maker.search.domain.Track
import com.example.yp_playlist_maker.utils.debounce
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class FavoriteTracksViewModel(private val likedTracksRepository: FavoriteTracksRepository): ViewModel() {
    private val context: Context by KoinJavaComponent.inject(Context::class.java)
    private val stateLiveData: MutableLiveData<Result<List<Track>>> by KoinJavaComponent.inject(
        MutableLiveData::class.java
    )

    companion object {
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 500L
    }

    fun observeState(): LiveData<Result<List<Track>>> = stateLiveData

    private fun renderState(state: Result<List<Track>>) {
        stateLiveData.postValue(state)
    }

    fun fillData(){
        viewModelScope.launch {
            likedTracksRepository
                .likedTracks()
                .collect{tracks ->
                    renderState(Result.success(tracks))
                }
        }
    }

    val onFavoriteTrackClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope, false){
        AudioPlayerActivity.show(context, it)
    }

}