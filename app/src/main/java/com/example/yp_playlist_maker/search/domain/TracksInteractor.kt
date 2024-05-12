package com.example.yp_playlist_maker.search.domain

import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>

    fun loadTrackData(expression: String): Track
}