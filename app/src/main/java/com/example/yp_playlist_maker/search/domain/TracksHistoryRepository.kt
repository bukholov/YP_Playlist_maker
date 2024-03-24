package com.example.yp_playlist_maker.search.domain

interface TracksHistoryRepository {
    fun clearSavedTracks()
    fun read(): List<Track>
    fun write(track: Track)
}