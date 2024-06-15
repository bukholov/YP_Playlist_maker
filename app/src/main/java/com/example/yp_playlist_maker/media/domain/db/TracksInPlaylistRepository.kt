package com.example.yp_playlist_maker.media.domain.db

import kotlinx.coroutines.flow.Flow

interface TracksInPlaylistRepository {
    suspend fun insertTrackInPlaylist(playlistId: Int, trackId: Int)

    fun getTrackIdsInPlaylist(playlistId: Int): Flow<List<Int>>
}