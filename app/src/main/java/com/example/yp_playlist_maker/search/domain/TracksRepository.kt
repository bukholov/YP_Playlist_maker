package com.example.yp_playlist_maker.search.domain

interface TracksRepository {
    fun searchTracks(expression: String): Result<List<Track>>
    fun loadTrackData(expression: String): Track
}