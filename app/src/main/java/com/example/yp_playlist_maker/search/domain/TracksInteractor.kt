package com.example.yp_playlist_maker.search.domain

import java.util.function.Consumer

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: Consumer<Result<List<Track>>>)

    fun loadTrackData(expression: String): Track
}