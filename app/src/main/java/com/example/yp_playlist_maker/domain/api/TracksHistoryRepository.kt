package com.example.yp_playlist_maker.domain.api

import com.example.yp_playlist_maker.domain.models.Track

interface TracksHistoryRepository {
    fun clearSavedTracks()
    fun read(): List<Track>
    fun write(track: Track)
}