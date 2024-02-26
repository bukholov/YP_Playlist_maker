package com.example.yp_playlist_maker.domain.impl

import com.example.yp_playlist_maker.domain.api.TracksHistoryInteractor
import com.example.yp_playlist_maker.domain.api.TracksHistoryRepository
import com.example.yp_playlist_maker.domain.models.Track

class TracksHistoryInteractorImpl(private val repository: TracksHistoryRepository):TracksHistoryInteractor {
    override fun clearSavedTracks() {
        repository.clearSavedTracks()
    }

    override fun read(): ArrayList<Track> {
        return repository.read()
    }

    override fun write(track: Track) {
        repository.write(track)
    }
}