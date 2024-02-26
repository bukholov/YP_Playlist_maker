package com.example.yp_playlist_maker.domain.api

import com.example.yp_playlist_maker.domain.models.Track

sealed interface TracksConsumerData<T> {
    data class Success<T>(val foundTracks: ArrayList<Track>) : TracksConsumerData<T>
    data class Error<T>(val message: String) : TracksConsumerData<T>
}

