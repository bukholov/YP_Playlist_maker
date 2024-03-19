package com.example.yp_playlist_maker.search.data

import android.view.View.OnClickListener
import com.example.yp_playlist_maker.search.domain.Track

sealed interface SearchState {
    object Loading: SearchState

    data class Content(
        val tracks: List<Track>
    ): SearchState

    data class Error(
        val onClickListener: OnClickListener
    ): SearchState

    data class Empty(
        val message: String
    ): SearchState
}