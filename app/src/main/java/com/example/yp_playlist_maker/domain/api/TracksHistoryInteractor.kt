package com.example.yp_playlist_maker.domain.api

import com.example.yp_playlist_maker.domain.models.Track

interface TracksHistoryInteractor {
    fun clearSavedTracks()
    fun read(): ArrayList<Track>
    fun write(track: Track)
}