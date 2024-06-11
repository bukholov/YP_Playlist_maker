package com.example.yp_playlist_maker.media.domain.db

import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun playlists(): Flow<List<Playlist>>

    suspend fun syncPlaylist(playlist: Playlist)
}