package com.example.yp_playlist_maker.media.domain.impl

import com.example.yp_playlist_maker.media.domain.db.TracksInPlaylistInteractor
import com.example.yp_playlist_maker.media.domain.db.TracksInPlaylistRepository
import kotlinx.coroutines.flow.Flow

class TracksInPlaylistInteractorImpl(private val tracksInPlaylistRepository: TracksInPlaylistRepository): TracksInPlaylistInteractor {
    override suspend fun insertTrackInPlaylist(playlistId: Int, trackId: Int) {
        tracksInPlaylistRepository.insertTrackInPlaylist(playlistId, trackId)
    }

    override fun getTrackIdsInPlaylist(playlistId: Int): Flow<List<Int>> {
        return tracksInPlaylistRepository.getTrackIdsInPlaylist(playlistId)
    }
}