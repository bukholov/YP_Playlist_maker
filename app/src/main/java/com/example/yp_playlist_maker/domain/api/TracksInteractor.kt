package com.example.yp_playlist_maker.domain.api

import com.example.yp_playlist_maker.domain.models.Track
import java.util.function.Consumer

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: Consumer<Result<List<Track>>>)
}