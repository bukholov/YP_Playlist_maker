package com.example.yp_playlist_maker.search.domain

import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Result<List<Track>>>
    fun loadTrackData(expression: String): Track
}